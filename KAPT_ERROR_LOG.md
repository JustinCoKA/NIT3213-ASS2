# Kapt IllegalAccessError 해결 로그

## 1. 문제 상황 (Problem)

프로젝트 빌드 시 `:app:kaptGenerateStubsDebugKotlin` 단계에서 다음과 같은 `IllegalAccessError`가 발생함:

```
java.lang.IllegalAccessError: superclass access check failed: class org.jetbrains.kotlin.kapt3.base.javac.KaptJavaCompiler (in unnamed module @0x340eb6e9) cannot access class com.sun.tools.javac.main.JavaCompiler (in module jdk.compiler) because module jdk.compiler does not export com.sun.tools.javac.main to unnamed module @0x340eb6e9
```

## 2. 발생 원인 (Cause)

- **JDK 버전 호환성**: JDK 16 이상부터는 Java 모듈 시스템(JPMS)에 의해 내부 API(`com.sun.tools.javac.*`)에 대한 접근이 기본적으로 차단됩니다.
- **Kapt의 내부 API 사용**: 이전 버전의 Kotlin Kapt는 컴파일 과정에서 JDK의 내부 `javac` API를 직접 사용하는데, 이를 위한 모듈 접근 권한이 설정되지 않아 발생한 문제입니다.

## 3. 해결 방법 (Solution)

문제를 해결하기 위해 두 가지 조치를 취했습니다.

### A. JVM 아규먼트 추가 (`gradle.properties`)
Gradle 데몬이 JDK 내부 모듈에 접근할 수 있도록 `org.gradle.jvmargs`에 `--add-exports` 및 `--add-opens` 옵션을 추가했습니다.

**수정 전:**
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
```

**수정 후:**
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED
```

### B. Kotlin 버전 업데이트 (`gradle/libs.versions.toml`)
최신 JDK와의 호환성을 높이기 위해 Kotlin 버전을 업데이트했습니다.

**수정 내용:**
- `kotlin = "1.9.0"` → `kotlin = "1.9.24"`

## 4. 결과 (Result)

- `./gradlew :app:kaptGenerateStubsDebugKotlin` 명령어가 정상적으로 수행됨을 확인했습니다.
- Gradle Sync 및 전체 빌드가 성공적으로 완료되었습니다.
