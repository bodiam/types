package kotools.types.number

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotools.assert.assertEquals
import kotools.assert.assertFailsWith
import kotools.assert.assertNotNull
import kotools.assert.assertNull
import kotools.types.core.RandomValueHolder
import kotlin.test.Test

class PositiveIntTest : RandomValueHolder {
    // ---------- Builders ----------

    @Test
    fun constructor_should_pass_with_a_positive_Int() {
        var value: Int = randomInt
        while (0 > value) value = randomInt
        val result = PositiveInt(value)
        result.value assertEquals value
    }

    @Test
    fun constructor_should_throw_an_error_with_a_strictly_negative_Int() {
        val value: Int = StrictlyNegativeInt.random.value
        assertFailsWith<IllegalArgumentException> { PositiveInt(value) }
    }

    @Test
    fun companion_orNull_should_pass_with_a_positive_Int() {
        var value: Int = randomInt
        while (0 > value) value = randomInt
        val result: PositiveInt? = PositiveInt orNull value
        result.assertNotNull().value assertEquals value
    }

    @Test
    fun companion_orNull_should_return_null_with_a_strictly_negative_Int() {
        val value: Int = StrictlyNegativeInt.random.value
        val result: PositiveInt? = PositiveInt orNull value
        result.assertNull()
    }

    @Test
    fun int_toPositiveInt_should_pass_with_a_positive_Int() {
        val value: Int = PositiveInt.random.value
        val result: PositiveInt = value.toPositiveInt()
        result.value assertEquals value
    }

    @Test
    fun int_toPositiveInt_should_throw_an_error_with_a_strictly_negative_Int() {
        val value: Int = StrictlyNegativeInt.random.value
        assertFailsWith<IllegalArgumentException>(value::toPositiveInt)
    }

    @Test
    fun int_toPositiveIntOrNull_should_pass_with_a_positive_Int() {
        val value: Int = PositiveInt.random.value
        val result: PositiveInt? = value.toPositiveIntOrNull()
        result?.value assertEquals value
    }

    @Test
    fun int_toPositiveIntOrNull_should_return_null_with_a_strictly_negative_Int() {
        val value: Int = StrictlyNegativeInt.random.value
        val result: PositiveInt? = value.toPositiveIntOrNull()
        result.assertNull()
    }

    @Test
    fun string_toPositiveInt_should_pass_with_a_String_representing_a_positive_number() {
        val value: Int = PositiveInt.random.value
        val valueAsString: String = value.toString()
        val result: PositiveInt = valueAsString.toPositiveInt()
        result.value assertEquals value
    }

    @Test
    fun string_toPositiveInt_should_throw_an_error_with_an_invalid_String() {
        assertFailsWith<NumberFormatException>("a"::toPositiveInt)
    }

    @Test
    fun string_toPositiveInt_should_throw_an_error_with_a_String_representing_a_strictly_negative_number() {
        val value: String = StrictlyNegativeInt.random.value.toString()
        assertFailsWith<IllegalArgumentException>(value::toPositiveInt)
    }

    @Test
    fun string_toPositiveIntOrNull_should_pass_with_a_String_representing_a_positive_number() {
        val value: Int = PositiveInt.random.value
        val valueAsString: String = value.toString()
        val result: PositiveInt? = valueAsString.toPositiveIntOrNull()
        result.assertNotNull().value assertEquals value
    }

    @Test
    fun string_toPositiveIntOrNull_should_return_null_with_an_invalid_String() {
        val result: PositiveInt? = "a".toPositiveIntOrNull()
        result.assertNull()
    }

    @Test
    fun string_toPositiveIntOrNull_should_return_null_with_a_String_representing_a_strictly_negative_number() {
        val value: String = StrictlyNegativeInt.random.value.toString()
        val result: PositiveInt? = value.toPositiveIntOrNull()
        result.assertNull()
    }

    // ---------- Serialization ----------

    @Test
    fun serialization_should_behave_like_an_Int() {
        val x: PositiveInt = PositiveInt.random
        val result: String = Json.encodeToString(x)
        result assertEquals Json.encodeToString(x.value)
    }

    @Test
    fun deserialization_should_pass() {
        val value: Int = PositiveInt.random.value
        val encoded: String = Json.encodeToString(value)
        val result: PositiveInt = Json.decodeFromString(encoded)
        result.value assertEquals value
    }

    // ---------- Unary operations ----------

    @Test
    fun inc_should_return_the_minimum_value_with_the_maximum_value() {
        var x: PositiveInt = PositiveInt.max
        x++
        x assertEquals PositiveInt.min
    }

    @Test
    fun inc_should_increment_the_value_by_1_with_an_initial_value_other_than_the_maximum_value() {
        var x: PositiveInt = PositiveInt.random
        while (x.value == PositiveInt.max.value) x = PositiveInt.random
        val initialValue: Int = x.value
        x++
        x.value assertEquals initialValue + 1
    }

    @Test
    fun dec_should_return_the_maximum_value_with_the_minimum_value() {
        var x: PositiveInt = PositiveInt.min
        x--
        x assertEquals PositiveInt.max
    }

    @Test
    fun dec_should_decrement_the_value_by_1_with_an_initial_value_other_than_the_minimum_value() {
        var x: PositiveInt = PositiveInt.random
        while (x.value == PositiveInt.min.value) x = PositiveInt.random
        val initialValue: Int = x.value
        x--
        x.value assertEquals initialValue - 1
    }

    @Test
    fun unaryMinus_should_pass() {
        val x: PositiveInt = PositiveInt.random
        val result: NegativeInt = -x
        result.value assertEquals -x.value
    }

    // ---------- Binary operations ----------

    @Test
    fun compareTo_should_pass_with_another_PositiveInt() {
        val x: PositiveInt = PositiveInt.random
        val y: PositiveInt = PositiveInt.random
        val result: Int = x.compareTo(y)
        result assertEquals x.value.compareTo(y.value)
    }

    // ---------- Conversions ----------

    @Test
    fun toNonZeroInt_should_pass_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        val result: NonZeroInt = x.toNonZeroInt()
        result.value assertEquals x.value
    }

    @Test
    fun toNonZeroInt_should_throw_an_error_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        assertFailsWith<IllegalArgumentException>(x::toNonZeroInt)
    }

    @Test
    fun toNonZeroIntOrNull_should_pass_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        val result: NonZeroInt? = x.toNonZeroIntOrNull()
        result.assertNotNull().value assertEquals x.value
    }

    @Test
    fun toNonZeroIntOrNull_should_return_null_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        val result: NonZeroInt? = x.toNonZeroIntOrNull()
        result.assertNull()
    }

    @Test
    fun toStrictlyPositiveInt_should_pass_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        val result: StrictlyPositiveInt = x.toStrictlyPositiveInt()
        result.value assertEquals x.value
    }

    @Test
    fun toStrictlyPositiveInt_should_throw_an_error_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        assertFailsWith<IllegalArgumentException>(x::toStrictlyPositiveInt)
    }

    @Test
    fun toStrictlyPositiveIntOrNull_should_pass_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        val result: StrictlyPositiveInt? = x.toStrictlyPositiveIntOrNull()
        result.assertNotNull().value assertEquals x.value
    }

    @Test
    fun toStrictlyPositiveIntOrNull_should_return_null_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        val result: StrictlyPositiveInt? = x.toStrictlyPositiveIntOrNull()
        result.assertNull()
    }

    @Test
    fun toNegativeInt_should_pass_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        val result: NegativeInt = x.toNegativeInt()
        result.value assertEquals x.value
    }

    @Test
    fun toNegativeInt_should_throw_an_error_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        assertFailsWith<IllegalArgumentException>(x::toNegativeInt)
    }

    @Test
    fun toNegativeIntOrNull_should_pass_with_a_value_that_equals_zero() {
        val x = PositiveInt(0)
        val result: NegativeInt? = x.toNegativeIntOrNull()
        result.assertNotNull().value assertEquals x.value
    }

    @Test
    fun toNegativeIntOrNull_should_return_null_with_a_strictly_positive_value() {
        val x = PositiveInt(StrictlyPositiveInt.random.value)
        val result: NegativeInt? = x.toNegativeIntOrNull()
        result.assertNull()
    }

    @Test
    fun toString_should_return_the_string_representation_of_the_value() {
        val x: PositiveInt = PositiveInt.random
        val result: String = x.toString()
        result assertEquals x.value.toString()
    }
}
