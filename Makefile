
check:
	./gradlew clean check bintrayUpload

publish: check
	./gradlew -PdryRun=false --info bintrayUpload
	./gradlew releng
