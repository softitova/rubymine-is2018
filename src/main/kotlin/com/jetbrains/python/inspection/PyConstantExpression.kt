package com.jetbrains.python.inspection


import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.*


class PyConstantExpression : PyInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean,
                              session: LocalInspectionToolSession): PsiElementVisitor {
        return Visitor(holder, session)
    }

    class Visitor(holder: ProblemsHolder?, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyIfStatement(node: PyIfStatement) {
            super.visitPyIfStatement(node)
            processIfPart(node.ifPart)
            for (part in node.elifParts) {
                processIfPart(part)
            }
        }

        private fun calculate(expr: PyExpression): Long? {
            fun get(ex: PyExpression) =
                    when (ex) {
                        is PyNumericLiteralExpression -> ex.longValue
                        is PyBinaryExpression -> calculate(ex)
                        else -> null
                    }

            return if (expr is PyBinaryExpression) {
                get(expr.leftExpression)?.let { l ->
                    get(expr.rightExpression!!)?.let { r ->
                        when (expr.operator?.specialMethodName) {
                            "__add__" -> (l + r)
                            "__sub__" -> (l - r)
                            "__mul__" -> (l * r)
                            else -> (l / r)
                        }
                    }
                }
            } else get(expr)

        }

        private fun processIfPart(pyIfPart: PyIfPart) {
            val condition = pyIfPart.condition

            fun registerProblem(value: Boolean) {
                registerProblem(condition, "The condition is always " + value)
            }

            if (condition is PyBoolLiteralExpression) {
                registerProblem(condition.value)
            } else if (condition is PyBinaryExpression) {
                calculate(condition.leftExpression)?.let { l ->
                    condition.rightExpression?.let {
                        calculate(it)?.let { r ->
                            when (condition.operator?.specialMethodName) {
                                "__eq__" -> registerProblem(l == r)
                                "__ne__" -> registerProblem(l != r)
                                "__lt__" -> registerProblem(l < r)
                                "__le__" -> registerProblem(l <= r)
                                "__gt__" -> registerProblem(l > r)
                                "__ge__" -> registerProblem(l >= r)
                            }
                        }
                    }
                }

            }
//            condition?.let {
//                ExpressionWalker.walk(it.text)?.let {
//                    registerProblem(condition, "The condition is always " + it)
//                }
//            }
        }
    }
}
