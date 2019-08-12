package io.github.corneil.spel

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.expression.EvaluationContext
import org.springframework.expression.PropertyAccessor
import org.springframework.expression.TypedValue
import org.springframework.expression.spel.SpelParserConfiguration
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

class PropertyHolder(val properties: Map<String, Any>, val name: String, val value1: Int, val value2: Double)
class SpelMapAccessor : PropertyAccessor {
    override fun getSpecificTargetClasses(): Array<Class<*>>? {
        return arrayOf(Map::class.java)
    }

    override fun canRead(context: EvaluationContext, target: Any?, name: String): Boolean {
        if (target is Map<*, *>) {
            return target.containsKey(name)
        }
        return false
    }

    override fun canWrite(context: EvaluationContext, target: Any?, name: String): Boolean {
        return false
    }

    override fun write(context: EvaluationContext, target: Any?, name: String, newValue: Any?) {
    }

    override fun read(context: EvaluationContext, target: Any?, name: String): TypedValue {
        if (target is Map<*, *>) {
            return TypedValue(target[name])
        }
        return TypedValue.NULL
    }
}

class SpelTests {
    @Test
    fun testSpel() {
        // val exp = "properties['name'] + ':Extra:' + properties['value1'] + ':' + properties['value2']"
        val exp = "properties.name + ':Extra:' + properties.value1 + ':' + properties.value2"
        // val exp = "name + ':Extra:' + value1 + ':' + value2"
        val properties = mapOf<String, Any>("name" to "joe", "value1" to 2, "value2" to 4.5)

        val expressionParser = SpelExpressionParser(SpelParserConfiguration())
        val expression = expressionParser.parseExpression(exp)
        val root = PropertyHolder(properties, "joe", 2, 4.5)
        // val context = SimpleEvaluationContext.forReadOnlyDataBinding().build()
        val context = StandardEvaluationContext()
        context.addPropertyAccessor(SpelMapAccessor())
        val output = expression.getValue(context, root, String::class.java)
        assertEquals(output, "joe:Extra:2:4.5")
    }
}
