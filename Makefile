
check:
	./gradlew clean check bintrayUpload --info

publish: check
	./gradlew -PdryRun=false --info bintrayUpload
	./gradlew releng
