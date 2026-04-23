# 🎓 Research University Management System

> Final Project — Object-Oriented Programming & Design  
> Kazakh-British Technical University

---

## 📌 About the Project

This is a **research-oriented university management system** built with Java as a final project for the OOP & Design course. The system simulates the real-world operations of a university — from student course registration and grading to academic research management and internal communications.

The project was designed and implemented following core OOP principles: abstraction, encapsulation, inheritance, and polymorphism, combined with industry-standard design patterns.

---

## ✨ Key Features

- 🔐 **Authentication** — every user accesses the system through a login system
- 📚 **Course Management** — registration, assignment, grading (major / minor / free elective)
- 🔬 **Research Module** — papers, projects, h-index calculation, journal subscriptions
- 📰 **News Feed** — with comments; research news is always pinned at the top
- 💬 **Internal Messaging** — employees can send messages and official notices to each other
- 🛠️ **Tech Support** — request tracking with statuses (NEW → VIEWED → ACCEPTED/REJECTED → DONE)
- 📊 **Reports & Statistics** — academic performance reports, top researchers, transcripts
- 🌐 **Multilingual Interface** — supports KZ, EN, RU

---

## 🏗️ Architecture

The system is designed using **UML Use Case and Class Diagrams** before implementation. It follows:

- **Object-oriented design** — superclasses, subclasses, abstract classes, interfaces, enumerations
- **Custom exceptions** — for business rule violations (e.g., invalid supervisor assignment)
- **Design Patterns** — 4+ patterns used throughout the system (Observer, Decorator, Singleton, Factory, etc.)
- **Serialization** — persistent data storage across sessions
- **Java Collections** — proper and logical use of Lists, Maps, Sets, etc.

---

## 👥 Team

| # | Role                 |
|---|----------------------|
| 1 | Zrazhevskiy Denis    |
| 2 | Gatiyatullin Ibragim |
| 3 | Yessentay Adil       |
| 4 | Kuanysh Zhanibek     |

> Team size: **4 developers**  
> Each member is responsible for a dedicated module of the system.

---

## 🗂️ Project Structure

```
src/
├── models/          # All entity classes (User, Student, Teacher, etc.)
├── interfaces/      # Interfaces (Researcher, Notifiable, etc.)
├── enums/           # Enumerations (CourseType, UrgencyLevel, RequestStatus, etc.)
├── exceptions/      # Custom exceptions
├── patterns/        # Design pattern implementations
├── utils/           # Helper classes, comparators, serialization
└── Main.java        # Entry point / console demo
```

---

## 🚀 How to Run

1. Clone the repository
2. Open the project in your IDE (IntelliJ IDEA recommended)
3. Build the project
4. Run `Main.java`
5. Log in with your credentials to access the system

---

## 📁 Submission

The final submission includes:

- 📄 **Report** (`.pdf`) — detailed description, UML diagrams, code fragments, problems faced
- 🗜️ **Project** (`.zip`) — source code with documentation
- 🖥️ **Presentation** (`.pdf`) — 3–4 slides: what works / what doesn't

---