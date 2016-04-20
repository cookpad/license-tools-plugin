# license-tool-plugin
OSSライセンスをチェックするプラグイン

## 使い方
1. `{project_root}/scripts/license_tools/data`配下に`aliases.yaml`と`libraries.yaml`を作成する
2. build.gradleに追記  
  project配下の各moduleをチェックするのでprojectのbuilg.gradleに書きましょう  
  downloadLicensesタスクがlicense-gradle-pluginに依存しているので併せて読み込みます

  ```
  buildscript {
     dependencies {    
        classpath 'nl.javadude.gradle.plugins:license-gradle-plugin:0.11.0'
        classpath 'com.cookpad.android:license-tool:0.2.0'  
     }  
  }  
  apply plugin: 'license'
  apply plugin: 'license-tool'  
  ```

3. gradleから実行する  

  `./gradlew clean downloadLicenses checkLicense`

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

## データ
### aliases.yaml
`dependency-license.xml`と実際のライセンスとの差を埋める為のファイルです

```
# 'No license found' in dependency-license.xml but actual license is 'Apache License 2.0'
activeandroid-0.0.20140611-bba53eb.jar: Apache License 2.0
# Ignore this library.
adsdk-2.1.0.aar:
```

### libraries.yaml
`licenses.html`作成用のデータソースです

```
- filename: line-chart-view-0.2.0.jar
  license: MIT
  name: 'line-chart-view'
  author: hogelog
  year: 2014
- filename: httpcore-4.3.2.jar
  license: Apache License 2.0
  name: Apache HttpCore
  notice: |
    Apache HttpComponents
    Copyright 2006-2014 The Apache Software Foundation

    This product includes software developed at
    The Apache Software Foundation (http://www.apache.org/).
- filename: butterknife-6.1.0.jar
  license: Apache License 2.0
  name: Butter Knife
  author: Jake Wharton
  year: 2013
```
