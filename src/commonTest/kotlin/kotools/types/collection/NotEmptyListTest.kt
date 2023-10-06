package kotools.types.collection

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotools.types.contentShouldEqual
import kotools.types.experimental.ExperimentalCollectionApi
import kotools.types.shouldBeNotNull
import kotools.types.shouldBeNull
import kotools.types.shouldEqual
import kotools.types.shouldFailWithIllegalArgumentException
import kotools.types.shouldFailWithSerializationException
import kotools.types.shouldHaveAMessage
import kotools.types.shouldNotEqual
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class NotEmptyListTest {
    @Test
    fun notEmptyListOf_should_pass() {
        val head: Int = Random.nextInt()
        val tail: Array<Int> = List(2) { Random.nextInt() }
            .toTypedArray()
        val result: NotEmptyList<Int> = notEmptyListOf(head, *tail)
        val expected: List<Int> = listOf(head) + tail
        result.toList() contentShouldEqual expected
    }

    @Test
    fun collection_toNotEmptyList_should_pass_with_a_not_empty_Collection() {
        val collection: Collection<Int> = List(3) { Random.nextInt() }
        val result: Result<NotEmptyList<Int>> = collection.toNotEmptyList()
        result.getOrThrow().toList() contentShouldEqual collection
    }

    @Test
    fun collection_toNotEmptyList_should_fail_with_an_empty_Collection() {
        val collection: Collection<Int> = emptyList()
        val result: Result<NotEmptyList<Int>> = collection.toNotEmptyList()
        result.shouldFailWithIllegalArgumentException { getOrThrow() }
            .shouldHaveAMessage()
    }

    @ExperimentalCollectionApi
    @Test
    fun toNotEmptyListOrNull_should_pass_with_a_not_empty_Collection() {
        val expected: Collection<Int> = List(3) { Random.nextInt() }
        val result: NotEmptyList<Int>? = expected.toNotEmptyListOrNull()
        val elements: NotEmptyList<Int> = assertNotNull(result)
        val actual: List<Int> = elements.toList()
        assertContentEquals(expected, actual)
    }

    @ExperimentalCollectionApi
    @Test
    fun toNotEmptyListOrNull_should_fail_with_an_empty_Collection() {
        val collection: Collection<Int> = emptyList()
        val result: NotEmptyList<Int>? = collection.toNotEmptyListOrNull()
        assertNull(result)
    }

    @ExperimentalCollectionApi
    @Test
    fun toNotEmptyListOrThrow_should_pass_with_a_not_empty_Collection() {
        val expected: Collection<Int> = List(3) { Random.nextInt() }
        val result: NotEmptyList<Int> = expected.toNotEmptyListOrThrow()
        val actual: List<Int> = result.toList()
        assertContentEquals(expected, actual)
    }

    @ExperimentalCollectionApi
    @Test
    fun toNotEmptyListOrThrow_should_fail_with_an_empty_Collection() {
        val collection: Collection<Int> = emptyList()
        val exception: IllegalArgumentException = assertFailsWith {
            collection.toNotEmptyListOrThrow()
        }
        val actualMessage: String = assertNotNull(exception.message)
        val expectedMessage: String = EmptyCollectionException.message
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun head_should_return_the_first_element_of_this_list() {
        val elements: NotEmptyList<Int> = List(3) { Random.nextInt() }
            .toNotEmptyList()
            .getOrThrow()
        val result: Int = elements.head
        result shouldEqual elements.toList().first()
    }

    @Test
    fun tail_should_return_all_elements_except_the_first_one() {
        val elements: NotEmptyList<Int> = List(3) { Random.nextInt() }
            .toNotEmptyList()
            .getOrThrow()
        val result: NotEmptyList<Int>? = elements.tail
        result.shouldBeNotNull()
            .toList() contentShouldEqual elements.toList().drop(1)
    }

    @Test
    fun tail_should_return_null_with_a_singleton_list() {
        val result: NotEmptyList<Int>? = notEmptyListOf(Random.nextInt()).tail
        result.shouldBeNull()
    }

    @Test
    fun equals_should_pass_with_the_same_NotEmptyList() {
        val x: NotEmptyList<Int> = notEmptyListOf(1, 2, 3)
        val y: NotEmptyList<Int> = x
        x shouldEqual y
    }

    @Test
    fun equals_should_pass_with_another_NotEmptyList_having_the_same_values() {
        val x: NotEmptyList<Int> = notEmptyListOf(1, 2, 3)
        val y: NotEmptyList<Int> = notEmptyListOf(1, 2, 3)
        x shouldEqual y
    }

    @Test
    fun equals_should_fail_with_another_NotEmptyList_having_another_head() {
        val x: NotEmptyList<Int> = notEmptyListOf(1, 2, 3)
        val y: NotEmptyList<Int> = notEmptyListOf(-1, 2, 3)
        x shouldNotEqual y
    }

    @Test
    fun equals_should_fail_with_another_NotEmptyList_having_another_tail() {
        val x: NotEmptyList<Int> = notEmptyListOf(1, 2, 3)
        val y: NotEmptyList<Int> = notEmptyListOf(1, -2, -3)
        x shouldNotEqual y
    }

    @Test
    fun toList_should_return_all_elements_as_a_List() {
        val elements: List<Int> = List(3) { Random.nextInt() }
        val result: List<Int> = elements.toNotEmptyList()
            .getOrThrow()
            .toList()
        result contentShouldEqual elements
    }

    @Test
    fun toString_should_behave_like_a_List() {
        val elements: NotEmptyList<Int> = List(3) { Random.nextInt() }
            .toNotEmptyList()
            .getOrThrow()
        "$elements" shouldEqual "${elements.toList()}"
    }
}

class NotEmptyListCompanionTest {
    @ExperimentalCollectionApi
    @Test
    fun of_should_pass_with_a_not_empty_Collection() {
        val collection: Collection<Int> = List(3) { Random.nextInt() }
        val result: NotEmptyList<Int>? = NotEmptyList.of(collection)
        result.shouldBeNotNull()
            .toList()
            .contentShouldEqual(collection)
    }

    @ExperimentalCollectionApi
    @Test
    fun of_should_fail_with_an_empty_Collection() {
        val collection: Collection<Int> = emptyList()
        val result: NotEmptyList<Int>? = NotEmptyList.of(collection)
        result.shouldBeNull()
    }
}

class NotEmptyListIntegrationTest {
    @Test
    fun updating_the_original_MutableCollection_should_not_impact_the_NotEmptyList() {
        val original: MutableCollection<Int> = mutableListOf(1, 2, 3)
        val notEmptyList: NotEmptyList<Int> = original.toNotEmptyList()
            .getOrThrow()
        "$original" shouldEqual "$notEmptyList"
        original.clear()
        "$original" shouldNotEqual "$notEmptyList"
    }
}

class NotEmptyListSerializerTest {
    @ExperimentalSerializationApi
    @Test
    fun descriptor_should_have_the_qualified_name_of_NotEmptyList_as_serial_name() {
        val elementSerializer: KSerializer<Int> = Int.serializer()
        val serializer: KSerializer<NotEmptyList<Int>> =
            NotEmptyList.serializer(elementSerializer)
        val result: String = serializer.descriptor.serialName
        val expected: String = ListSerializer(elementSerializer)
            .descriptor
            .serialName
        result shouldEqual expected
    }

    @Test
    fun serialization_should_behave_like_a_List() {
        val elements: NotEmptyList<Int> = List(3) { Random.nextInt() }
            .toNotEmptyList()
            .getOrThrow()
        val result: String = Json.encodeToString(elements)
        val expected: String = Json.encodeToString(elements.toList())
        result shouldEqual expected
    }

    @Test
    fun deserialization_should_pass_with_a_not_empty_Collection() {
        val collection: Collection<Int> = List(3) { Random.nextInt() }
        val encoded: String = Json.encodeToString(collection)
        val result: NotEmptyList<Int> = Json.decodeFromString(encoded)
        result.toList() contentShouldEqual collection
    }

    @Test
    fun deserialization_should_fail_with_an_empty_Collection() {
        val collection: Collection<Int> = emptyList()
        val encoded: String = Json.encodeToString(collection)
        Json.shouldFailWithSerializationException {
            decodeFromString<NotEmptyList<Int>>(encoded)
        }.shouldHaveAMessage()
    }
}
