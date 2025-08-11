# 🛍️ Product Showcase App

A modern Android application built with **Jetpack Compose**, showcasing a list of products with detailed views, bookmarking functionality, and real-time search. This project demonstrates clean architecture, scalable design patterns, and best practices in Android development.

---
## 🧰 Tech Stack

| Layer              | Tools & Libraries                          |
|-------------------|---------------------------------------------|
| UI                | Jetpack Compose, Material Design            |
| Architecture      | MVVM or MVI, Clean Architecture             |
| Networking        | Retrofit, Coroutines                        |
| Dependency Injection | Hilt                                   |
| Persistence       | Room Database                               |
| Async Data Flow   | Kotlin Flow or LiveData                     |
| Testing (Planned) | Unit Tests, Integration Tests               |

---

## 🧪 Testing Roadmap

Testing is planned for future iterations:
- ✅ Unit Tests (ViewModels, UseCases)
- ✅ Integration Tests (Repository, API, DB interactions)

---

## 🧼 Best Practices

This project emphasizes:
- Clean Code principles
- Separation of concerns
- Scalable modular architecture
- Maintainable and readable codebase

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
    https://github.com/AlirezaHr/Product.git
2. Open in Android Studio (Arctic Fox or newer).
3. Sync Gradle and run the app on an emulator or device.

## 📂 Project Structure
```
├── data
│   ├── local             # Room database, DAOs, entities
│   ├── mapper            # Data-to-domain model mappers
│   ├── api               # Retrofit interfaces and DTOs
│   └── repository        # Data layer implementations (local + remote)
├── domain
│   ├── model             # Core business models used across layers
│   ├── repository        # Abstract repository interfaces
│   └── usecase           # Business logic and orchestration
├── presentation
│   ├── list              # Product list screen (UI + ViewModel)
│   ├── detail            # Product detail screen (UI + ViewModel)
│   └── components        # Reusable Compose UI components
├── di                    # Hilt modules for dependency injection
└── MainActivity.kt       # Navigation host and app entry point


