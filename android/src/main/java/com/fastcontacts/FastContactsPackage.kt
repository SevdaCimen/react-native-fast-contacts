package com.fastcontacts

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class FastContactsPackage : TurboReactPackage() {
  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    return if (name == FastContactsModule.NAME) {
      FastContactsModule(reactContext)
    } else {
      null
    }
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      val moduleInfos = mutableMapOf<String, ReactModuleInfo>()
      moduleInfos[FastContactsModule.NAME] = ReactModuleInfo(
        FastContactsModule.NAME,
        FastContactsModule.NAME,
        false, // canOverrideExistingModule
        false, // needsEagerInit
        true,  // hasConstants
        false, // isCxxModule
        true   // isTurboModule
      )
      moduleInfos
    }
  }
}