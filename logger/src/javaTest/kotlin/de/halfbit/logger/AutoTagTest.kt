/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlin.test.Test
import kotlin.test.assertContentEquals

class AutoTagTest {

    @Test
    fun autoTag_declaredInClass() {
        testAutoTag(tagInClass, "AutoTagTest")
    }

    @Test
    fun autoTag_declaredInInnerClass() {
        val inner = Inner()
        testAutoTag(inner.tag, "AutoTagTest.Inner")
    }

    @Test
    fun autoTag_declaredInMethod() {
        val tag = autoTag()
        testAutoTag(tag, "AutoTagTest")
    }

    @Test
    fun autoTag_declaredInFile() {
        testAutoTag(tagInFile, "AutoTagTest")
    }

    @Test
    fun autoTag_declaredInCompanion() {
        testAutoTag(tagInCompanion, "AutoTagTest")
    }

    @Test
    fun autoTag_declaredInObject() {
        testAutoTag(Object.tag, "AutoTagTest.Object")
    }

    @Test
    fun autoTag_declaredInInnerObject() {
        testAutoTag(Object.Inner.tag, "AutoTagTest.Object.Inner")
    }

    // test configuration

    private val tagInClass = autoTag()

    companion object {
        val tagInCompanion = autoTag()
    }

    object Object {
        object Inner {
            val tag = autoTag()
        }

        val tag = autoTag()
    }

    class Inner {
        val tag = autoTag()
    }
}

private val tagInFile = autoTag()

private lateinit var memoryRingSink: MemoryRingSink
internal fun testAutoTag(autoTag: LogTag, expected: String) {
    initializeLogger {
        memoryRingSink = registerMemoryRingSink(
            logPrinter = { _, tag, _, _, _ -> tag }
        )
    }
    d(autoTag) { "" }
    val actual = memoryRingSink.getLogEntries()
    assertContentEquals(listOf(expected), actual)
}
