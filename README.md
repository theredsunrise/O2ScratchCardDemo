# O2 Scratch Card Demo

Implementované zadanie O2 demo ako Kotlin Multiplatform projekt, ktorý je funkčný pre Android aj iOS platformu.
Sofistikovanejším riešením pre dlhotrvajúcu úlohu pri aktivácii karty by bolo použitie WorkManagera. V tomto prípade je však úloha riešená prostredníctvom singletonu.

- Android: minimum SDK 28 (Android 9), compile/target SDK 37
- iOS: deployment target 18.2
- Architektúra: Clean Architecture, Koin, MVI s view model, Kotlin Multiplatform, Compose Multiplatform a Kotlin Flow
- Testy: Kotlin Test/JUnit, Kotest, Mokkery (KMP verzia mockk), Turbine

Build apky: ./gradlew :androidApp:assembleDebug
Spustenie testov: ./gradlew :shared:testAndroidHostTest 