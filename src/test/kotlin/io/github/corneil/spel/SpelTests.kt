package io.github.corneil.spel

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.core.convert.TypeDescriptor
import org.springframework.expression.spel.SpelParserConfiguration
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.SimpleEvaluationContext

class PropertyHolder(val properties: Map<String, Any>)

class SpelTests {
    @Test
    fun testSpel() {
        val exp = "properties.name + ':Extra:' + properties.value1 + ':' + properties.value2"
        val properties = mapOf<String, Any>("name" to "joe", "value1" to 2, "value2" to 4.5)

        val expressionParser = SpelExpressionParser(SpelParserConfiguration())
        val expression = expressionParser.parseExpression(exp)
        val root = PropertyHolder(properties)
        val context = SimpleEvaluationContext.forReadOnlyDataBinding().withTypedRootObject(
            root, TypeDescriptor.valueOf(PropertyHolder::class.java)
        ).build()
        val output = expression.getValue(context, String::class)
        assertEquals(output, "joe:Extra:2:4.5")
    }
}
