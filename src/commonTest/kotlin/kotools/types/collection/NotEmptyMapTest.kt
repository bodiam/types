package kotools.types.collection

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotools.types.contentShouldEqual
import kotools.types.number.StrictlyPositiveInt
import kotools.types.shouldBeNotNull
import kotools.types.shouldBeNull
import kotools.types.shouldEqual
import kotools.types.shouldFailWithIllegalArgumentException
import kotools.types.shouldHaveAMessage
import kotools.types.shouldNotEqual
import kotlin.random.Random
import kotlin.test.*

class NotEmptyMapTest {
    @Test
    fun notEmptyMapOf_should_pass() {
        val head: Pair<Char, Int> = 'a' to Random.nextInt()
        val tail: Array<Pair<Char, Int>> = arrayOf(
            'b' to Random.nextInt(),
            'c' to Random.nextInt()
        )
        val result: NotEmptyMap<Char, Int> = notEmptyMapOf(head, *tail)
        val expected: Map<Char, Int> = mapOf(head, *tail)
        result.entries.toSet() contentShouldEqual expected.entries
    }

    @Test
    fun map_toNotEmptyMap_should_pass_with_a_not_empty_Map() {
        val map: Map<Char, Int> = mapOf('a' to Random.nextInt())
        val result: Result<NotEmptyMap<Char, Int>> = map.toNotEmptyMap()
        result.getOrThrow()
            .toMap()
            .entries contentShouldEqual map.entries
    }

    @Test
    fun map_toNotEmptyMap_should_fail_with_an_empty_Map() {
        val map: Map<Char, Int> = emptyMap()
        val result: Result<NotEmptyMap<Char, Int>> = map.toNotEmptyMap()
        result.shouldFailWithIllegalArgumentException { getOrThrow() }
            .shouldHaveAMessage()
    }

    @Test
    fun head_should_return_its_first_entry() {
        val expected: Pair<Char, Int> = 'a' to 1
        val map: NotEmptyMap<Char, Int> = notEmptyMapOf(expected, 'b' to 2)
        val result: Pair<Char, Int> = map.head
        result shouldEqual expected
    }

    @Test
    fun tail_should_return_all_its_entries_except_the_first_one() {
        val expected: NotEmptyMap<Char, Int> = notEmptyMapOf('b' to 2)
        val map: NotEmptyMap<Char, Int> = expected.entries.toSet()
            .map { it.toPair() }
            .toTypedArray()
            .let { notEmptyMapOf('a' to 1, *it) }
        val result: NotEmptyMap<Char, Int>? = map.tail
        result.shouldBeNotNull() shouldEqual expected
    }

    @Test
    fun tail_should_return_null_with_a_singleton_map() {
        val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1)
        val result: NotEmptyMap<Char, Int>? = map.tail
        result.shouldBeNull()
    }

    @Test
    fun entries_should_return_all_entries_of_this_map_as_a_NotEmptySet() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        val result: NotEmptySet<Map.Entry<String, Int>> = notEmptyMap.entries
        val expected: Set<Map.Entry<String, Int>> = notEmptyMap.toMap().entries
        result.toSet() contentShouldEqual expected
    }

    @Test
    fun keys_should_return_all_keys_of_this_map_as_a_NotEmptySet() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        val result: NotEmptySet<String> = notEmptyMap.keys
        val expected: Set<String> = notEmptyMap.toMap().keys
        result.toSet() contentShouldEqual expected
    }

    @Test
    fun values_should_return_all_values_of_this_map_as_a_NotEmptyList() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        val result: NotEmptyList<Int> = notEmptyMap.values
        val expected: Collection<Int> = notEmptyMap.toMap().values
        result.toList() contentShouldEqual expected
    }

    @Test
    fun size_should_return_the_size_of_this_map_as_a_StrictlyPositiveInt() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        val result: StrictlyPositiveInt = notEmptyMap.size
        val expected: Int = notEmptyMap.toMap().size
        result.toInt() shouldEqual expected
    }

    @Test
    fun equals_should_pass_with_the_same_NotEmptyMap() {
        val x: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
        val y: NotEmptyMap<Char, Int> = x
        x shouldEqual y
    }

    @Test
    fun equals_should_pass_with_another_NotEmptyMap_having_the_same_values() {
        val x: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
        val y: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
        x shouldEqual y
    }

    @Test
    fun equals_should_fail_with_another_NotEmptyMap_having_another_head() {
        val x: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
        val y: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to -1, 'b' to 2)
        x shouldNotEqual y
    }

    @Test
    fun equals_should_fail_with_another_NotEmptyMap_having_another_tail() {
        val x: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
        val y: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to -2)
        x shouldNotEqual y
    }

    @Test
    fun toString_should_behave_like_a_Map() {
        val notEmptyMap: NotEmptyMap<Char, Int> = notEmptyMapOf(
            'a' to Random.nextInt(),
            'b' to Random.nextInt(),
            'c' to Random.nextInt()
        )
        val expected: Map<Char, Int> = notEmptyMap.toMap()
        "$notEmptyMap" shouldEqual "$expected"
    }

}

class NotEmptyMapIntegrationTest {
    @Test
    fun updating_the_original_MutableMap_should_not_impact_the_NotEmptyMap() {
        val original: MutableMap<Char, Int> = mutableMapOf('a' to 1, 'b' to 2)
        val notEmptyMap: NotEmptyMap<Char, Int> = original.toNotEmptyMap()
            .getOrThrow()
        assertEquals("$original", "$notEmptyMap")

        original.clear()
        assertNotEquals("$original", "$notEmptyMap")
    }
}

class NotEmptyMapSerializerTest {
    @ExperimentalSerializationApi
    @Test
    fun descriptor_should_have_the_qualified_name_of_NotEmptyMap_as_serial_name() {
        // GIVEN
        val keySerializer: KSerializer<String> = String.serializer()
        val valueSerializer: KSerializer<Int> = Int.serializer()
        val serializer: KSerializer<NotEmptyMap<String, Int>> =
            NotEmptyMap.serializer(keySerializer, valueSerializer)
        // WHEN
        val actual: String = serializer.descriptor.serialName
        // THEN
        val expected: String = MapSerializer(keySerializer, valueSerializer)
            .descriptor
            .serialName
        assertEquals(expected, actual)
    }

    @Test
    fun serialization_should_behave_like_a_Map() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        assertEquals(
            actual = Json.encodeToString(notEmptyMap),
            expected = Json.encodeToString(notEmptyMap.toMap())
        )
    }

    @Test
    fun deserialization_should_pass_with_a_not_empty_Map() {
        val notEmptyMap: NotEmptyMap<String, Int> = notEmptyMapOf(
            "a" to Random.nextInt(),
            "b" to Random.nextInt(),
            "c" to Random.nextInt()
        )
        val encoded: String = Json.encodeToString(notEmptyMap)
        assertEquals(
            actual = Json.decodeFromString<NotEmptyMap<String, Int>>(encoded)
                .toMap(),
            expected = notEmptyMap.toMap()
        )
    }

    @Test
    fun deserialization_should_fail_with_an_empty_Map() {
        val map: Map<String, Int> = emptyMap()
        val encoded: String = Json.encodeToString(map)
        assertFailsWith<SerializationException> {
            Json.decodeFromString<NotEmptyMap<String, Int>>(encoded)
        }.shouldHaveAMessage()
    }
}

private fun <K, V> assertEquals(actual: Map<K, V>, expected: Map<K, V>) {
    assertEquals(actual = actual.size, expected = expected.size)
    actual.forEach {
        assertTrue { expected.containsKey(it.key) }
        assertEquals(actual = it.value, expected = expected[it.key])
    }
}
