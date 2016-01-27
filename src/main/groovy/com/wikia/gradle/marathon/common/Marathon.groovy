package com.wikia.gradle.marathon.common

import groovy.transform.AutoClone
import mesosphere.marathon.client.model.v2.UpgradeStrategy

import static com.wikia.gradle.marathon.common.ConfigResolver.dslToParamClosure
import static com.wikia.gradle.marathon.common.ConfigResolver.resolveNullConfig

@AutoClone
class Marathon implements Validating {

    String prodUrl
    String devUrl
    Boolean useProd
    Closure rawUpgradeStrategy
    Closure rawLabels

    def validate() {
        if (this.properties.get("prodUrl") == null) {
            throw new RuntimeException("Marathon.devUrl needs to be set")
        }

        if (this.properties.get("devUrl") == null) {
            throw new RuntimeException("Marathon.prodUrl needs to be set")
        }
    }

    def upgradeStrategy(Closure upgradeStrategy) {
        setUpgradeStrategy(upgradeStrategy)
    }

    def setUpgradeStrategy(Closure upgradeStrategy) {
        this.rawUpgradeStrategy = dslToParamClosure(upgradeStrategy)
    }

    UpgradeStrategy resolveUpgradeStrategy() {
        return resolveNullConfig(this.rawUpgradeStrategy, UpgradeStrategy)
    }

    def labels(Closure labels) {
        setLabels(labels)
    }

    def setLabels(Closure labels) {
        this.rawLabels = dslToParamClosure(labels)
    }

    Labels resolveLabels() {
        return resolveNullConfig(this.rawLabels, Labels)
    }

    def getUrl() {
        if (useProd) {
            return prodUrl
        } else {
            return devUrl
        }
    }
}