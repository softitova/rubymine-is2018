/**
 * Created by Sophia Titova on 12.11.17.
 */
package kotlin.com.jetbrains.python.inspection

import antlr.generated.ExpressionLexer
import antlr.generated.ExpressionParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

/**
 * Created by Titova Sophia on 29.10.17.
 */
class ExpressionWalker() {

    companion object {

        fun walk(text: String) =
                try {
                    ExpressionParser(
                            CommonTokenStream(
                                    ExpressionLexer(
                                            ANTLRInputStream(
                                                    text.byteInputStream()))))
                            .parse()
                } catch (e: Throwable) {
                    println("ERROR WITH TEXT : $text")
                    println(e.stackTrace)
                }!!
    }

}

fun main(args: Array<String>) {
    val INPUT_FILE = "input.txt"
    ExpressionWalker.walk(File(INPUT_FILE).readText())
}