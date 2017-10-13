# Android License Tools Plugin [![CircleCI](https://circleci.com/gh/cookpad/license-tools-plugin.svg?style=svg)](https://circleci.com/gh/cookpad/license-tools-plugin) [ ![Download](https://api.bintray.com/packages/cookpad-inc/maven/license-tools-plugin/images/download.svg) ](https://bintray.com/cookpad-inc/maven/license-tools-plugin/_latestVersion)

Gradle Plugin to check library licenses and generate license pages.

* `./gradlew checkLicenses` to check licenses in dependencies
* `./gradlew generateLicensePage` to generate a license page `licenses.html`
* `./gradlew generateLicenseJson` to generate a license json file `licenses.json`

## Setup

This plugin requires JDK8 (1.8.0 or later).

```gradle
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.cookpad.android.licensetools:license-tools-plugin:0.24.0'
    }
}

apply plugin: 'com.cookpad.android.licensetools'
```

See [example/build.gradle] for example.

## How To Use

### Run the `checkLicenses` task

You will see the following messages by `./gradlew checkLicenses`:

```yaml
# Libraries not listed:
- artifact: com.android.support:support-v4:+
  name: #NAME#
  copyrightHolder: #AUTHOR#
  license: No license found
- artifact: com.android.support:animated-vector-drawable:+
  name: #NAME#
  copyrightHolder: #AUTHOR#
  license: No license found
- artifact: io.reactivex:rxjava:+
  name: #NAME#
  copyrightHolder: #AUTHOR#
  license: apache2
 ```
 
### Add library licenses to `app/licenses.yml`

Then, Create `app/licenses.yml`, and add libraries listed the above with required fields:

```yaml
- artifact: com.android.support:+:+
  name: Android Support Libraries
  copyrightHolder: The Android Open Source Project
  license: apache2
- artifact: io.reactivex:rxjava:+
  name: RxJava
  copyrightHolder: Netflix, Inc.
  license: apache2
```

You can use wildcards in artifact names and versions.
You'll know the Android support libraries are grouped in `com.android.support` so you use `com.android.support:+:+` here.

Then, `./gradlew checkLicenses` will passes.

### Generate `licenses.html` by the `generateLicensePage` task

`./gradlew generateLicensePage` generates `app/src/main/assets/licenses.html`.

This plugin does not provide `Activity` nor `Fragment` to show `licenses.html`. You should add it by yourself.

`example/MainActivity` is an example.

### Configuring the plugin

Use `licenseTools` in your build.gradle to add some optional configuration.

For example:
```
licenseTools {
    outputHtml = "licenses_output.html"
}
```

Available configuration fields:

| Field name      | Default value      | Description   | 
| -------------   | -------------      | ------------- |
| `licensesYaml`  | `"licenses.yml"`   | The name of the licenses yml file                                                                         |
| `outputHtml`    | `"licenses.html"`  | The file name of the output of the `generateLicensePage` task                                             |
| `outputJson`    | `"licenses.json"`  | The file name of the output of the `generateLicenseJson` task                                             |
| `ignoredGroups` | `[]` (empty array) | An array of group names the plugin will ignore (useful for internal dependencies with missing .pom files) |

## DataSet Format

### Required Fields

* `artifact`
* `name`
* Either `copyrightHolder`, `author`, `authors` or `notice`

### Optional Fields

* `year` to indicate copyright years
* `skip` to skip generating license entries (for proprietary libraries)

### Example

```yaml
- artifact: com.android.support:+:+
  name: Android Support Libraries
  copyrightHolder: The Android Open Source Project
  license: apache2
- artifact: org.abego.treelayout:org.abego.treelayout.core:+
  name: abego TreeLayout
  copyrightHolder: abego Software
  license: bsd_3_clauses
- artifact: io.reactivex:rxjava:+
  name: RxJava
  copyrightHolder: Netflix, Inc.
  license: apache2
- artifact: com.tunnelvisionlabs:antlr4-runtime:4.5
  name: ANTLR4
  authors:
    - Terence Parr
    - Sam Harwell
  license: bsd_3_clauses
- artifact: com.github.gfx.android.orma:+:+
  name: Android Orma
  notice: |
    Copyright (c) 2015 FUJI Goro (gfx)
    SQLite.g4 is: Copyright (c) 2014 by Bart Kiers
  license: apache_2
- artifact: io.reactivex:rxandroid:1.1.0
  name: RxAndroid
  copyrightHolder: The RxAndroid authors
  license: apache2
- artifact: license-tools-plugin:example-dep:+
  skip: true
```

## See Also

- [オープンソースライセンスの管理を楽にする -Android アプリ編 - クックパッド開発者ブログ](http://techlife.cookpad.com/entry/2016/04/28/183000)

## For Developers

### Release Engineering

To bump versions:

```sh
./gradlew bumpPatch
./gradlew bumpMinor
./gradlew bumpMajor
```

To test artifacts:

```
make check
```

To publish artifacts:

```sh
make publish
```

Keep `CHANGES.md` up-to-date.

## Copyright and License

Copyright (c) 2016 Cookpad Inc.

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
