package com.abdulmateen.pos_offline.utils

import kotlin.test.Test
import kotlin.test.assertEquals


class ValidatorTest {
    @Test
    fun validateNonEmpty() {
        val validator = Validator.validateNonEmpty("test")
        assertEquals(true, validator.isValid)
    }

    @Test
    fun validateNonEmptyEmpty() {
        val validator = Validator.validateNonEmpty("")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validatePassword() {
        val validator = Validator.validatePassword("test")
        assertEquals(true, validator.isValid)
    }

    @Test
    fun validatePasswordEmpty() {
        val validator = Validator.validatePassword("")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validateConfirmPassword() {
        val validator = Validator.validateConfirmPassword("test", "test")
        assertEquals(true, validator.isValid)
    }

    @Test
    fun validateConfirmPasswordEmpty() {
        val validator = Validator.validateConfirmPassword("", "")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validateEmail() {
        val validator = Validator.validateEmail("asd@gmail.com")
        assertEquals(true, validator.isValid)
    }

    @Test
    fun validateEmailEmpty() {
        val validator = Validator.validateEmail("")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validateEmailInvalid() {
        val validator = Validator.validateEmail("asdgmail.com")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validateEmailInvalid2() {
        val validator = Validator.validateEmail("asd@.com")
        assertEquals(false, validator.isValid)
    }

    @Test
    fun validateEmailInvalid3() {
        val validator = Validator.validateEmail("asd@.com")
        assertEquals(false, validator.isValid)
    }


}