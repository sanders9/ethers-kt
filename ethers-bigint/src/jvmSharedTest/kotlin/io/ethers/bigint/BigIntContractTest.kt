package io.ethers.bigint

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BigIntContractTest : FunSpec({
    context("constants") {
        test("ZERO / ONE / TWO / TEN have expected numeric values") {
            BigInts.ZERO shouldBe BigInt("0")
            BigInts.ONE shouldBe BigInt("1")
            BigInts.TWO shouldBe BigInt("2")
            BigInts.TEN shouldBe BigInt("10")
        }
    }

    context("string parsing") {
        test("decimal strings round-trip") {
            BigInt("12345678901234567890").toString() shouldBe "12345678901234567890"
        }

        test("hex strings parsed with radix") {
            BigInt("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16) shouldBe
                BigInt("115792089237316195423570985008687907853269984665640564039457584007913129639935")
        }

        test("toString(radix) round-trips") {
            BigInt("ABCDEF", 16).toString(16) shouldBe "abcdef"
        }
    }

    context("valueOf") {
        test("Long values map to BigInt") {
            BigInts.valueOf(0L) shouldBe BigInts.ZERO
            BigInts.valueOf(Long.MAX_VALUE) shouldBe BigInt("9223372036854775807")
            BigInts.valueOf(-128L).signum() shouldBe -1
        }
    }

    context("arithmetic") {
        val a = BigInt("100")
        val b = BigInt("3")

        test("add") { a.add(b) shouldBe BigInt("103") }
        test("subtract") { a.subtract(b) shouldBe BigInt("97") }
        test("multiply") { a.multiply(b) shouldBe BigInt("300") }
        test("divide (truncating)") { a.divide(b) shouldBe BigInt("33") }
        test("negate") { a.negate() shouldBe BigInt("-100") }
        test("pow") { BigInts.TEN.pow(4) shouldBe BigInt("10000") }
    }

    context("signum / bitLength") {
        test("signum") {
            BigInts.ZERO.signum() shouldBe 0
            BigInts.ONE.signum() shouldBe 1
            BigInts.ONE.negate().signum() shouldBe -1
        }

        test("bitLength matches the JVM contract") {
            BigInts.ZERO.bitLength() shouldBe 0
            BigInts.ONE.bitLength() shouldBe 1
            BigInt("ff", 16).bitLength() shouldBe 8
            BigInt("100", 16).bitLength() shouldBe 9
        }
    }

    context("compareTo / min / max") {
        val small = BigInt("1")
        val large = BigInt("1000")

        test("compareTo") {
            small.compareTo(large) shouldBe -1
            large.compareTo(small) shouldBe 1
            small.compareTo(small) shouldBe 0
            (small < large) shouldBe true
            (large >= small) shouldBe true
        }

        test("min / max") {
            small.min(large) shouldBe small
            small.max(large) shouldBe large
        }
    }

    context("byte-array round-trips") {
        test("toByteArray uses two's-complement (sign bit)") {
            BigInt("ff", 16).toByteArray() shouldBe byteArrayOf(0x00, 0xff.toByte())
            BigInt("7f", 16).toByteArray() shouldBe byteArrayOf(0x7f)
            BigInts.ZERO.toByteArray() shouldBe byteArrayOf(0x00)
        }

        test("fromUnsignedBytes treats input as positive magnitude") {
            BigInts.fromUnsignedBytes(byteArrayOf(0xff.toByte())) shouldBe BigInt("255")
            BigInts.fromUnsignedBytes(byteArrayOf(0xff.toByte(), 0xff.toByte())) shouldBe BigInt("65535")
        }

        test("fromSignedBytes interprets the sign bit") {
            BigInts.fromSignedBytes(byteArrayOf(0xff.toByte())) shouldBe BigInt("-1")
            BigInts.fromSignedBytes(byteArrayOf(0x7f)) shouldBe BigInt("127")
        }

        test("offset / length slice the input array") {
            val padded = byteArrayOf(0x00, 0x00, 0x12, 0x34, 0x00, 0x00)
            BigInts.fromUnsignedBytes(padded, offset = 2, length = 2) shouldBe BigInt("4660")
            BigInts.fromSignedBytes(padded, offset = 2, length = 2) shouldBe BigInt("4660")
        }

        test("32-byte unsigned word with leading sign byte") {
            val word = ByteArray(32) { 0xff.toByte() }
            val result = BigInts.fromUnsignedBytes(word)
            result.signum() shouldBe 1
            result shouldBe BigInt("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16)
        }
    }

    context("number extensions") {
        test("Int.toBigInt / Long.toBigInt") {
            42.toBigInt() shouldBe BigInt("42")
            (-42L).toBigInt() shouldBe BigInt("-42")
        }

        test("String.toBigInt / String.toBigInt(radix)") {
            "123".toBigInt() shouldBe BigInt("123")
            "ff".toBigInt(16) shouldBe BigInt("255")
        }
    }

    context("BigDecimal bridge") {
        test("BigInt.toBigDecimal(scale) divides by 10^scale") {
            BigInt("1000000000000000000").toBigDecimal(18).toPlainString() shouldBe "1.000000000000000000"
            BigInt("123456").toBigDecimal(3).toPlainString() shouldBe "123.456"
            BigInts.ZERO.toBigDecimal(0).toPlainString() shouldBe "0"
        }
    }
})
