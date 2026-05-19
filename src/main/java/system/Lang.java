package system;

import enums.Language;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Localization utility. Stores all UI strings in EN/KZ/RU.
 * Usage: Lang.get("key")
 */
public final class Lang implements Serializable {

    private static Language current = Language.EN;

    private static final Map<String, String[]> TEXTS = new HashMap<>();

    static {
        // Index: 0=EN, 1=KZ, 2=RU
        // ── Common ──
        put("welcome",          "Welcome",                      "Қош келдіңіз",             "Добро пожаловать");
        put("logout",           "Logout",                       "Шығу",                     "Выйти");
        put("back",             "Back",                         "Артқа",                    "Назад");
        put("choice",           "Choice",                       "Таңдау",                   "Выбор");
        put("invalid",          "Invalid option",               "Қате таңдау",              "Неверный выбор");
        put("email",            "Email",                        "Электрондық пошта",        "Эл. почта");
        put("password",         "Password",                     "Құпия сөз",               "Пароль");
        put("login",            "Login",                        "Кіру",                     "Вход");
        put("login_fail",       "Invalid email or password",    "Қате email немесе пароль", "Неверный email или пароль");
        put("saving",           "Saving data...",               "Деректер сақталуда...",    "Сохранение данных...");
        put("lang_select",      "Choose language",              "Тілді таңдаңыз",          "Выберите язык");
        put("yes",              "yes",                          "иә",                       "да");
        put("cancel",           "Cancelled",                    "Болдырмау",                "Отменено");
        put("confirm",          "Confirm? (yes/no)",            "Растайсыз ба? (иә/жоқ)",  "Подтвердить? (да/нет)");
        put("not_found",        "Not found",                    "Табылмады",                "Не найдено");
        put("success",          "Success",                      "Сәтті",                    "Успешно");
        put("error",            "Error",                        "Қате",                     "Ошибка");
        put("empty",            "Empty",                        "Бос",                      "Пусто");
        put("message",          "Message",                      "Хабарлама",                "Сообщение");
        put("sent",             "Sent",                         "Жіберілді",                "Отправлено");
        put("select",           "Select",                       "Таңдаңыз",                "Выберите");
        put("title",            "Title",                        "Тақырып",                  "Заголовок");
        put("content",          "Content",                      "Мазмұны",                  "Содержание");

        // ── System ──
        put("sys_title",        "KBTU University System",       "KBTU Университет Жүйесі", "Система Университета KBTU");
        put("role",             "Role",                         "Рөлі",                     "Роль");

        // ── Admin ──
        put("admin_menu",       "ADMIN MENU",                   "ӘКІМШІ МӘЗІРІ",           "МЕНЮ АДМИНИСТРАТОРА");
        put("admin_users",      "View all users",               "Барлық қолданушылар",      "Все пользователи");
        put("admin_add",        "Add user",                     "Қолданушы қосу",           "Добавить пользователя");
        put("admin_remove",     "Remove user",                  "Қолданушыны жою",          "Удалить пользователя");
        put("admin_update",     "Update user email",            "Email жаңарту",            "Обновить email");
        put("admin_reset",      "Reset user password",          "Құпия сөзді қалпына келтіру", "Сбросить пароль");
        put("admin_search",     "Search user by ID",            "ID бойынша іздеу",         "Поиск по ID");
        put("admin_logs",       "View system logs",             "Жүйе логтары",            "Журнал системы");
        put("admin_filter",     "Filter logs by action",        "Логтарды сүзу",           "Фильтр логов");
        put("admin_courses",    "View all courses",             "Барлық курстар",           "Все курсы");

        // ── Student ──
        put("stu_menu",         "STUDENT MENU",                 "СТУДЕНТ МӘЗІРІ",          "МЕНЮ СТУДЕНТА");
        put("stu_courses",      "My courses",                   "Менің курстарым",          "Мои курсы");
        put("stu_register",     "Request course registration",  "Курсқа тіркелу өтініші",  "Заявка на курс");
        put("stu_drop",         "Drop course",                  "Курсты тастау",            "Отчислиться с курса");
        put("stu_marks",        "View marks",                   "Бағаларды көру",           "Посмотреть оценки");
        put("stu_transcript",   "View transcript",              "Транскрипт",               "Транскрипт");
        put("stu_rate",         "Rate teacher",                 "Оқытушыны бағалау",        "Оценить преподавателя");
        put("stu_teacher_info", "View teacher info",            "Оқытушы туралы ақпарат",   "Информация о преподавателе");
        put("stu_news",         "View news",                    "Жаңалықтар",               "Новости");
        put("stu_comment",      "Comment on news",              "Жаңалыққа пікір",          "Комментировать новость");
        put("stu_journal_sub",  "Subscribe to journal",         "Журналға жазылу",          "Подписаться на журнал");
        put("stu_journals",     "View journals",                "Журналдар",                "Журналы");
        put("stu_orgs",         "My organizations",             "Менің ұйымдарым",          "Мои организации");
        put("stu_join_org",     "Join organization",            "Ұйымға кіру",             "Вступить в организацию");
        put("stu_leave_org",    "Leave organization",           "Ұйымнан шығу",            "Покинуть организацию");
        put("stu_pending",      "My pending requests",          "Күтудегі өтініштер",       "Мои заявки");
        put("stu_no_courses",   "No courses yet",               "Курстар жоқ",              "Курсов пока нет");

        // ── Teacher ──
        put("tch_menu",         "TEACHER MENU",                 "ОҚЫТУШЫ МӘЗІРІ",          "МЕНЮ ПРЕПОДАВАТЕЛЯ");
        put("tch_courses",      "View my courses",              "Менің курстарым",          "Мои курсы");
        put("tch_students",     "View students in course",      "Курстағы студенттер",      "Студенты курса");
        put("tch_marks",        "Put marks",                    "Баға қою",                 "Выставить оценки");
        put("tch_complaint",    "Send complaint",               "Шағым жіберу",             "Отправить жалобу");
        put("tch_send_msg",     "Send message to employee",     "Қызметкерге хабарлама",    "Сообщение сотруднику");
        put("tch_inbox",        "View inbox",                   "Кіріс хабарламалар",       "Входящие");
        put("tch_news",         "View news",                    "Жаңалықтар",               "Новости");
        put("tch_comment",      "Comment on news",              "Пікір жазу",               "Комментировать");

        // ── Manager ──
        put("mgr_menu",         "MANAGER MENU",                 "МЕНЕДЖЕР МӘЗІРІ",         "МЕНЮ МЕНЕДЖЕРА");
        put("mgr_assign_lec",   "Assign lecture teacher",       "Лекция оқытушысын тағайындау", "Назначить лектора");
        put("mgr_assign_prac",  "Assign practice teacher",      "Практика оқытушысын тағайындау", "Назначить практика");
        put("mgr_approve",      "Approve/Reject registrations", "Тіркелу өтініштері",      "Заявки на регистрацию");
        put("mgr_report",       "Generate course report",       "Курс есебі",              "Отчет по курсу");
        put("mgr_school_rep",   "Generate school report",       "Мектеп есебі",            "Отчет по школе");
        put("mgr_stu_gpa",      "Students sorted by GPA",       "Студенттер GPA бойынша",  "Студенты по GPA");
        put("mgr_stu_name",     "Students sorted by name",      "Студенттер аты бойынша",  "Студенты по имени");
        put("mgr_tch_rate",     "Teachers sorted by rating",    "Оқытушылар рейтинг бойынша", "Преподаватели по рейтингу");
        put("mgr_news_create",  "Create news",                  "Жаңалық құру",            "Создать новость");
        put("mgr_news_view",    "View news",                    "Жаңалықтарды көру",       "Просмотр новостей");
        put("mgr_news_del",     "Delete news",                  "Жаңалықты жою",           "Удалить новость");
        put("mgr_send_msg",     "Send message",                 "Хабарлама жіберу",        "Отправить сообщение");
        put("mgr_inbox",        "View inbox",                   "Кіріс хабарламалар",      "Входящие");
        put("mgr_official",     "Send official message",        "Ресми хат жіберу",        "Отправить оф. письмо");
        put("mgr_emp_req",      "View employee requests",       "Қызметкер өтініштері",    "Заявки сотрудников");
        put("mgr_assign_course","Assign course to teacher",     "Курсты оқытушыға тағайындау", "Назначить курс преподавателю");

        // ── Tech Support ──
        put("tech_menu",        "TECH SUPPORT MENU",            "ТЕХ. ҚОЛДАУ МӘЗІРІ",     "МЕНЮ ТЕХ. ПОДДЕРЖКИ");
        put("tech_new",         "View NEW requests",            "ЖАҢА өтініштер",          "НОВЫЕ заявки");
        put("tech_all",         "View ALL requests",            "БАРЛЫҚ өтініштер",        "ВСЕ заявки");
        put("tech_accept",      "Accept request",               "Өтінішті қабылдау",       "Принять заявку");
        put("tech_reject",      "Reject request",               "Өтінішті қабылдамау",     "Отклонить заявку");
        put("tech_done",        "Mark as DONE",                 "ОРЫНДАЛДЫ деп белгілеу",  "Отметить ВЫПОЛНЕННЫМ");
        put("tech_filter",      "Filter by status",             "Статус бойынша сүзу",     "Фильтр по статусу");
        put("tech_summary",     "Summary dashboard",            "Жиынтық панелі",          "Сводная панель");

        // ── Research ──
        put("res_menu",         "RESEARCHER MENU",              "ЗЕРТТЕУШІ МӘЗІРІ",       "МЕНЮ ИССЛЕДОВАТЕЛЯ");
        put("res_add",          "Add research paper",           "Зерттеу мақаласын қосу",  "Добавить статью");
        put("res_view",         "View my papers",               "Менің мақалаларым",       "Мои статьи");
        put("res_cite",         "Get citation",                 "Дәйексөз алу",            "Получить цитирование");
        put("res_hindex",       "My h-index",                   "Менің h-индексім",        "Мой h-индекс");
        put("res_join",         "Join research project",        "Зерттеу жобасына қосылу", "Вступить в проект");
        put("res_projects",     "My research projects",         "Менің зерттеу жобаларым", "Мои проекты");
        put("res_publish",      "Publish to journal",           "Журналға жариялау",       "Опубликовать в журнале");
        put("res_subscribe",    "Subscribe to journal",         "Журналға жазылу",         "Подписаться на журнал");
        put("res_top",          "Top researchers",              "Үздік зерттеушілер",      "Топ исследователей");
        put("res_all_papers",   "All university papers",        "Университет мақалалары",  "Все статьи университета");
    }

    private static void put(String key, String en, String kz, String ru) {
        TEXTS.put(key, new String[]{en, kz, ru});
    }

    public static void setLanguage(Language lang) {
        current = lang;
    }

    public static Language getLanguage() {
        return current;
    }

    public static String get(String key) {
        String[] vals = TEXTS.get(key);
        if (vals == null) return key;
        int idx = switch (current) {
            case EN -> 0;
            case KZ -> 1;
            case RU -> 2;
        };
        return vals[idx];
    }

    // ── Formatting helpers ──

    public static void header(String text) {
        int len = Math.max(text.length() + 4, 40);
        String line = "═".repeat(len);
        System.out.println("\n╔" + line + "╗");
        System.out.println("║  " + text + " ".repeat(len - text.length() - 2) + "║");
        System.out.println("╚" + line + "╝");
    }

    public static void separator() {
        System.out.println("─".repeat(44));
    }

    public static void menuItem(int num, String key) {
        System.out.printf("  %2d │ %s%n", num, get(key));
    }

    public static void menuItem(int num, String text, boolean raw) {
        System.out.printf("  %2d │ %s%n", num, text);
    }

    public static void menuExit() {
        System.out.printf("   0 │ %s%n", get("logout"));
    }

    public static void menuBack() {
        System.out.printf("   0 │ %s%n", get("back"));
    }

    public static void prompt() {
        System.out.print("➤ " + get("choice") + ": ");
    }

    public static void info(String msg) {
        System.out.println("  ℹ " + msg);
    }

    public static void ok(String msg) {
        System.out.println("  ✓ " + msg);
    }

    public static void err(String msg) {
        System.out.println("  ✗ " + msg);
    }

    public static void warn(String msg) {
        System.out.println("  ⚠ " + msg);
    }
}
