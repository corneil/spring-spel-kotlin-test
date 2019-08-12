# Spring Spel Kotlin Test
This project illustrates a problem when trying to use Spel with Kotlin

```shell script
./gradlew test
```

```kotlin
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
```

Test output:
```
org.springframework.expression.spel.SpelEvaluationException: EL1008E: Property or field 'properties' cannot be found on object of type 'kotlin.reflect.jvm.internal.KClassImpl' - maybe not public or not valid?

	at org.springframework.expression.spel.ast.PropertyOrFieldReference.readProperty(PropertyOrFieldReference.java:217)
	at org.springframework.expression.spel.ast.PropertyOrFieldReference.getValueInternal(PropertyOrFieldReference.java:104)
	at org.springframework.expression.spel.ast.PropertyOrFieldReference.getValueInternal(PropertyOrFieldReference.java:91)
	at org.springframework.expression.spel.ast.CompoundExpression.getValueRef(CompoundExpression.java:53)
	at org.springframework.expression.spel.ast.CompoundExpression.getValueInternal(CompoundExpression.java:89)
	at org.springframework.expression.spel.ast.OpPlus.getValueInternal(OpPlus.java:83)
	at org.springframework.expression.spel.ast.OpPlus.getValueInternal(OpPlus.java:83)
	at org.springframework.expression.spel.ast.OpPlus.getValueInternal(OpPlus.java:83)
	at org.springframework.expression.spel.ast.OpPlus.getValueInternal(OpPlus.java:83)
	at org.springframework.expression.spel.ast.SpelNodeImpl.getValue(SpelNodeImpl.java:109)
	at org.springframework.expression.spel.standard.SpelExpression.getValue(SpelExpression.java:328)
	at io.github.corneil.spel.SpelTests.testSpel(SpelTests.kt:24)
```

Initially I tried `propererties` directly as the root.
