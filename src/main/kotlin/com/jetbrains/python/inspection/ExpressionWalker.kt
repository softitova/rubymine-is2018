package com.jetbrains.python.inspection

import antlr.generated.ExpressionLexer
import antlr.generated.ExpressionParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

/**
 * Created by Sophia Titova on 22.04.18.
 */
class ExpressionWalker() {

    companion object {

        public fun walk(text: String) =
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