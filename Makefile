
check:
	./gradlew clean check bintrayUpload --info

publish: check
	./gradlew clean
	./gradlew -PdryRun=false --info bintrayUpload
	./gradlew releng
