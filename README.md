# license-tool-plugin
OSSライセンスをチェックするプラグイン

## 使い方
1. build.gradle(project)に追記  
  project配下の各moduleをチェックするのでapply分はprojectのbuilg.gradleに書きましょう  

  ```
  buildscript {
     dependencies {    
        classpath 'com.cookpad.android:license-tool:0.2.0'  
     }  
  }  
  apply plugin: 'license-tool'  
  ```

2. gradleから実行する  

  `./gradlew downloadLicenses checkLicense`

成功時は以下の様なレスポンスが表示されます
```
:checkLicense
OK
```

失敗時は以下のように表示されビルドが失敗しますので`aliases.yaml`と`libraries.yaml`を確認してください
```
:checkLicense
# Not used libraries:
- filename: sample-1.0.0.aar
  license: [No license found]
check aliases.yaml
```
