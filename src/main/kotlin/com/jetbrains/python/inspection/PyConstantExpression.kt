package com.jetbrains.python.inspection


import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.impl.PyBinaryExpressionImpl


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

        private fun processIfPart(pyIfPart: PyIfPart) {
            val condition = pyIfPart.condition
            fun registerProblem(value: Boolean) {
                registerProblem(condition, "The condition is always " + value)
            }
            if (condition is PyBoolLiteralExpression) {
                registerProblem(condition.value)
            } else if (condition is PyBinaryExpression) {
                val lExpr = condition.leftExpression
                val rExpr = condition.rightExpression
                if (lExpr is PyNumericLiteralExpression) {
                    lExpr.longValue?.let { l ->
                        if (rExpr is PyNumericLiteralExpression)
                            rExpr.longValue?.let { r ->
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
