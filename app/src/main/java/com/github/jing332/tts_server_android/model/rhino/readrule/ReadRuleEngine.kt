package com.github.jing332.tts_server_android.model.rhino.readrule

import android.content.Context
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.data.entities.ReadRule
import com.github.jing332.tts_server_android.model.rhino.core.BaseScriptEngine
import com.github.jing332.tts_server_android.model.rhino.core.BaseScriptEngineContext
import com.github.jing332.tts_server_android.model.rhino.core.Logger

class ReadRuleEngine(
    val context: Context,
    private val rule: ReadRule,
    override var code: String = rule.code,
    override val logger: Logger = Logger.global
) :
    BaseScriptEngine(ttsrvObject = BaseScriptEngineContext(context = context, "ReadRule")) {
    companion object {
        const val OBJ_JS = "ReadRuleJS"

        const val FUNC_HANDLE_TEXT = "handleText"
    }

    private val objJS
        get() = findObject(OBJ_JS)

    @Suppress("UNCHECKED_CAST")
    fun evalInfo() {
        eval()
        objJS.apply {
            rule.name = get("name").toString()
            rule.ruleId = get("id").toString()
            rule.author = get("author").toString()

            rule.tags = get("tags") as Map<String, String>

            runCatching {
                rule.version = (get("version") as Double).toInt()
            }.onFailure {
                throw NumberFormatException(context.getString(R.string.plugin_bad_format))
            }
        }
    }

    fun handleText(text: String): List<TextWithTag> {
        val resultList: MutableList<TextWithTag> = mutableListOf()
        rhino.invokeMethod(objJS, FUNC_HANDLE_TEXT, text)
            ?.run { this as List<*> }
            ?.let { list ->
                list.forEach {
                    if (it is Map<*, *>) {
                        resultList.add(TextWithTag(it["text"].toString(), it["tag"].toString()))
                    }
                }

            }
        return resultList
    }

    data class TextWithTag(val text: String, val tag: String)
}