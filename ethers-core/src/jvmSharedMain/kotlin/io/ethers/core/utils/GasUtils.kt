package io.ethers.core.utils

import io.ethers.bigint.BigInt
import io.ethers.bigint.BigInts
import io.ethers.core.utils.GasUtils.getEffectiveGasTip

object GasUtils {
    /**
     * Get how much will be paid as transaction gas tip based on [baseFee], [gasTipCap], and [gasFeeCap] constraints.
     * */
    @JvmStatic
    @Suppress("UnnecessaryVariable")
    fun getEffectiveGasTip(baseFee: BigInt, gasTipCap: BigInt, gasFeeCap: BigInt): BigInt {
        val possibleTip = gasFeeCap - baseFee
        val maxTip = gasTipCap

        return maxTip.min(possibleTip)
    }

    /**
     * Get how much will be paid as transaction gas price. This is the sum of [baseFee] and [getEffectiveGasTip].
     * */
    @JvmStatic
    fun getEffectiveGasPrice(baseFee: BigInt, gasTipCap: BigInt, gasFeeCap: BigInt): BigInt {
        return baseFee + getEffectiveGasTip(baseFee, gasTipCap, gasFeeCap)
    }
}
