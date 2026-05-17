# Kapt IllegalAccessError Resolution Log

## 1. Problem

The following `IllegalAccessError` occurred during the `:app:kaptGenerateStubsDebugKotlin` task of the project build:

```
java.lang.IllegalAccessError: superclass access check failed: class org.jetbrains.kotlin.kapt3.base.javac.KaptJavaCompiler (in unnamed module @0x340eb6e9) cannot access class com.sun.tools.javac.main.JavaCompiler (in module jdk.compiler) because module jdk.compiler does not export com.sun.tools.javac.main to unnamed module @0x340eb6e9
```

## 2. Cause

- **JDK Version Compatibility**: Starting from JDK 16, access to internal APIs (`com.sun.tools.javac.*`) is blocked by default due to the Java Platform Module System (JPMS).
- **Kapt's Internal API Usage**: Older versions of Kotlin Kapt directly use JDK internal `javac` APIs during the compilation process, leading to access errors when the necessary module permissions are not granted.

## 3. Solution

Two steps were taken to resolve this issue.

### A. Adding JVM Arguments (`gradle.properties`)
Added `--add-exports` and `--add-opens` options to `org.gradle.jvmargs` to allow the Gradle daemon to access internal JDK modules.

**Before:**
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
```

**After:**
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED
```

### B. Updating Kotlin Version (`gradle/libs.versions.toml`)
Updated the Kotlin version to improve compatibility with modern JDKs.

**Update:**
- `kotlin = "1.9.0"` → `kotlin = "1.9.24"`

## 4. Result

- Verified that the `./gradlew :app:kaptGenerateStubsDebugKotlin` command runs successfully.
- Gradle Sync and the full project build completed without issues.
