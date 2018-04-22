/**
 * Created by Sophia Titova on 12.11.17.
 */
package com.jetbrains.python.inspection

import antlr.generated.ExpressionLexer
import antlr.generated.ExpressionParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

/**
 * Created by Titova Sophia on 29.10.17.
 */
class ExpressionWalker() {

    companion object {

        fun walk(text: String) =
                (try {
                ExpressionParser(
                        CommonTokenStream(
                                ExpressionLexer(
                                        ANTLRInputStream(
                                                text.byteInputStream()))))
                        .parse()
            } catch (e: Throwable) {
                println("Error during parsing condition : $text")
                null
            })?.result
        }

}

fun main(args: Array<String>) {
    val INPUT_FILE = "input.txt"
    print(ExpressionWalker.walk("1 - 1 ==  0 - 3 * 3 + 1"))
}