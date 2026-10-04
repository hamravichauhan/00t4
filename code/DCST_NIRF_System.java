import java.io.*;
import java.lang.reflect.*;
import java.util.*;

// ============================================================================
// FEATURE 2: Enums & Static Tracking
// ============================================================================
enum DepartmentRole {
    TEACHING_FACULTY, NON_TEACHING_STAFF, RESEARCH_SCHOLAR, STUDENT
}

enum ActivityCategory {
    ACADEMIC, NON_ACADEMIC
}

// Custom Exception for Requirement 9
class InvalidNIRFDataException extends Exception {
    public InvalidNIRFDataException(String message) {
        super(message);
    }
}

// ============================================================================
// FEATURE 5 & 6: Interfaces, Class as a Type & Message Passing
// ============================================================================
interface NIRFReportable {
    void generateNIRFSummary();
}

interface Auditor {
    void auditCompliance();
}

interface QualityAssurance {
    void verifyNBAAccreditation();
}

// Multiple Interface Inheritance (Feature 3)
interface ComprehensiveAuditor extends Auditor, QualityAssurance {
    void submitConsolidatedAudit();
}

// ============================================================================
// FEATURE 3: Multiple Implementation Inheritance (Default Methods)
// ============================================================================
interface ResearchMetricsTrackable {
    default void logResearchOutput() {
        System.out.println("[RESEARCH LOG] Scopus & Web of Science citations synced with NIRF cell.");
    }
}

interface OutreachMetricsTrackable {
    default void logCommunityOutreach() {
        System.out.println("[OUTREACH LOG] State-level community training events registered.");
    }
}

// ============================================================================
// FEATURE 1, 4, 10: Object Referencing, Access Modifiers & Cloning
// ============================================================================
class DepartmentAffiliation implements Serializable, Cloneable {
    public String campus = "Taleigao Plateau";
    protected String school = "Goa Business School (GBS)";
    private String department = "Department of Computer Science & Technology (DCST)";

    public DepartmentAffiliation() {}

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String dept) {
        this.department = dept;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

// Base Abstract Class (Feature 5)
abstract class Member implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    // Feature 2: Static Counter
    private static int totalMemberCount = 0;

    // Feature 4: Access Modifiers
    public String id;
    protected String name;
    private double confidentialScore;
    DepartmentRole role;
    DepartmentAffiliation affiliation; // Used to demonstrate shallow vs deep clone

    // Feature 1: Constructor initializing all attributes
    public Member(String id, String name, double confidentialScore, DepartmentRole role, DepartmentAffiliation affiliation) {
        this.id = id;
        this.name = name;
        this.confidentialScore = confidentialScore;
        this.role = role;
        this.affiliation = affiliation;
        totalMemberCount++;
    }

    // Feature 2: Static Method
    public static int getTotalMembers() {
        return totalMemberCount;
    }

    public static void displayTotalMembers() {
        System.out.println("Total DCST Members Registered for NIRF: " + totalMemberCount);
    }

    // Feature 4: Controlled access to private member
    public double getConfidentialScore() {
        return this.confidentialScore;
    }

    public void setConfidentialScore(double score) {
        this.confidentialScore = score;
    }

    // Feature 5: Abstract Method
    public abstract void printProfile();

    // Feature 10: Shallow Clone
    public Member shallowClone() throws CloneNotSupportedException {
        return (Member) super.clone();
    }

    // Feature 10: Deep Clone
    public Member deepClone() throws CloneNotSupportedException {
        Member cloned = (Member) super.clone();
        if (this.affiliation != null) {
            cloned.affiliation = (DepartmentAffiliation) this.affiliation.clone();
        }
        return cloned;
    }
}

// ============================================================================
// FEATURE 3, 5, 7: Derived Classes, Polymorphism & Inheritance
// ============================================================================
class Faculty extends Member implements NIRFReportable, ResearchMetricsTrackable, OutreachMetricsTrackable {
    private String designation;
    private String specialization;

    public Faculty(String id, String name, double score, String designation, String specialization, DepartmentAffiliation aff) {
        super(id, name, score, DepartmentRole.TEACHING_FACULTY, aff);
        this.designation = designation;
        this.specialization = specialization;
    }

    // Feature 7: Overriding
    @Override
    public void printProfile() {
        System.out.println("Faculty [ID: " + id + ", Name: " + name + ", Role: " + designation + 
                           ", Specialization: " + specialization + ", Campus: " + affiliation.campus + "]");
    }

    @Override
    public void generateNIRFSummary() {
        System.out.println("NIRF Metric TLR (Teaching, Learning & Resources): Faculty " + name + 
                           " registered with specialization: " + specialization);
    }

    // Feature 7: Overloading
    public void logEvaluation(String examType) {
        System.out.println("Faculty " + name + " submitted marks for exam component: " + examType);
    }

    public void logEvaluation(String examType, int studentCount) {
        System.out.println("Faculty " + name + " submitted " + examType + " marks for " + studentCount + " enrolled students.");
    }
}

class Student extends Member implements NIRFReportable {
    private String program; // MCA, M.Tech, M.Sc
    private int yearOfEnrollment;

    public Student(String id, String name, double score, String program, int yearOfEnrollment, DepartmentAffiliation aff) {
        super(id, name, score, DepartmentRole.STUDENT, aff);
        this.program = program;
        this.yearOfEnrollment = yearOfEnrollment;
    }

    @Override
    public void printProfile() {
        System.out.println("Student [Roll: " + id + ", Name: " + name + ", Program: " + program + 
                           ", Batch: " + yearOfEnrollment + "]");
    }

    @Override
    public void generateNIRFSummary() {
        System.out.println("NIRF Metric GO (Graduation Outcomes): Student " + name + 
                           " enrolled in " + program + " [" + affiliation.getDepartment() + "]");
    }
}

class NonTeachingStaff extends Member implements NIRFReportable {
    private String responsibilities;

    public NonTeachingStaff(String id, String name, double score, String responsibilities, DepartmentAffiliation aff) {
        super(id, name, score, DepartmentRole.NON_TEACHING_STAFF, aff);
        this.responsibilities = responsibilities;
    }

    @Override
    public void printProfile() {
        System.out.println("Staff [ID: " + id + ", Name: " + name + ", Responsibility: " + responsibilities + "]");
    }

    @Override
    public void generateNIRFSummary() {
        System.out.println("NIRF Metric Support Resources: Operational duty handled by " + name);
    }
}

// Activity Class for Collections and Records
class DepartmentActivity {
    String name;
    ActivityCategory category;
    String description;

    public DepartmentActivity(String name, ActivityCategory category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-15s : %s", category, name, description);
    }
}

// ============================================================================
// FEATURE 8: Generics Implementation
// ============================================================================
class GenericRepository<T> {
    private List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public List<T> getAll() {
        return items;
    }

    // Generic Method
    public static <E> void printGenericCollection(Collection<E> collection, String header) {
        System.out.println("\n--- " + header + " ---");
        for (E elem : collection) {
            System.out.println(" * " + elem.toString());
        }
    }
}

// ============================================================================
// MAIN EXECUTION ENGINE
// ============================================================================
public class DCST_NIRF_System {

    // Feature 6: Message Passing through Interface Type
    public static void dispatchToNIRFPortal(NIRFReportable reportableEntity) {
        System.out.print("[Portal Dispatch] ");
        reportableEntity.generateNIRFSummary();
    }

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("  GOA UNIVERSITY - DCST / GBS NIRF DATA VERIFICATION SYSTEM");
        System.out.println("========================================================================\n");

        DepartmentAffiliation mainCampusAffiliation = new DepartmentAffiliation();

        // --------------------------------------------------------------------
        // 1. OBJECT REFERENCING & CONSTRUCTORS
        // --------------------------------------------------------------------
        System.out.println(">>> 1. DEMONSTRATING OBJECT REFERENCING & CONSTRUCTORS");
        Faculty profA = new Faculty("F-01", "Dr. V. V. Kamat", 98.5, "Professor", "AI & Data Engineering", mainCampusAffiliation);
        Faculty profB = profA; // Shared reference

        System.out.println("Original Faculty Name: " + profA.name);
        System.out.println("Aliased Reference Name: " + profB.name);

        profB.name = "Prof. V. V. Kamat (Senior Dean)";
        System.out.println("After mutating via profB -> profA.name is now: " + profA.name + " (Proves shared reference effect)\n");

        // --------------------------------------------------------------------
        // 2. STATIC ATTRIBUTES, METHODS & ENUMS
        // --------------------------------------------------------------------
        System.out.println(">>> 2. DEMONSTRATING STATIC ATTRIBUTES & ENUM COUNTERS");
        Faculty fac2 = new Faculty("F-02", "Dr. J. Pawar", 92.0, "Associate Professor", "Cloud Computing & Systems", mainCampusAffiliation);
        Student stu1 = new Student("MCA-2401", "Ravi Chauhan", 89.4, "MCA", 2024, mainCampusAffiliation);
        Student stu2 = new Student("MTECH-2402", "Sneha Naik", 91.0, "M.Tech CSE", 2024, mainCampusAffiliation);
        NonTeachingStaff staff1 = new NonTeachingStaff("S-101", "Mr. Ramesh Shet", 75.0, "Systems Administrator & Lab In-charge", mainCampusAffiliation);

        Member.displayTotalMembers();
        System.out.println();

        // --------------------------------------------------------------------
        // 3. MULTIPLE INHERITANCE (Interface & Implementation via Default Methods)
        // --------------------------------------------------------------------
        System.out.println(">>> 3. DEMONSTRATING MULTIPLE INHERITANCE");
        // Multiple implementation inheritance through default methods
        profA.logResearchOutput();
        profA.logCommunityOutreach();

        // Multiple interface inheritance demonstration
        ComprehensiveAuditor auditor = new ComprehensiveAuditor() {
            @Override
            public void auditCompliance() {
                System.out.println("[Auditor] NIRF teaching-learning criteria compliant.");
            }
            @Override
            public void verifyNBAAccreditation() {
                System.out.println("[Auditor] Tier-1 NBA validation verified for PG programs.");
            }
            @Override
            public void submitConsolidatedAudit() {
                auditCompliance();
                verifyNBAAccreditation();
                System.out.println("[Auditor] Consolidated DCST audit signed and logged.");
            }
        };
        auditor.submitConsolidatedAudit();
        System.out.println();

        // --------------------------------------------------------------------
        // 4. ACCESS MODIFIERS
        // --------------------------------------------------------------------
        System.out.println(">>> 4. DEMONSTRATING ACCESS MODIFIERS");
        System.out.println("Public Member (id): " + stu1.id);
        System.out.println("Protected Member (name): " + stu1.name);
        // Direct access to private member would cause compilation error:
        // System.out.println(stu1.confidentialScore); // COMPILE ERROR
        System.out.println("Private Member (accessed via public getter): " + stu1.getConfidentialScore() + "\n");

        // --------------------------------------------------------------------
        // 5 & 7. POLYMORPHISM (OVERRIDING & OVERLOADING)
        // --------------------------------------------------------------------
        System.out.println(">>> 5 & 7. DEMONSTRATING POLYMORPHISM (Dynamic Method Dispatch & Overloading)");
        Member[] membersArray = {profA, fac2, stu1, stu2, staff1};
        for (Member m : membersArray) {
            m.printProfile(); // Dynamic method overriding dispatch
        }
        // Overloading
        profA.logEvaluation("ISA-1 (Individual Test)");
        profA.logEvaluation("SEA (Semester End Assessment)", 45);
        System.out.println();

        // --------------------------------------------------------------------
        // 6. CLASS AS A TYPE & MESSAGE PASSING
        // --------------------------------------------------------------------
        System.out.println(">>> 6. DEMONSTRATING MESSAGE PASSING TO INTERFACE TYPE");
        dispatchToNIRFPortal(profA);
        dispatchToNIRFPortal(stu1);
        dispatchToNIRFPortal(staff1);
        System.out.println();

        // --------------------------------------------------------------------
        // 8. GENERICS & COLLECTION FRAMEWORKS
        // --------------------------------------------------------------------
        System.out.println(">>> 8. DEMONSTRATING GENERICS & JAVA COLLECTIONS (List, Queue, Set, Map)");

        // Generic Repository
        GenericRepository<DepartmentActivity> activityRepo = new GenericRepository<>();

        // Academic Activities
        activityRepo.add(new DepartmentActivity("ISA", ActivityCategory.ACADEMIC, "Intra-Semester Assessment continuous evaluation"));
        activityRepo.add(new DepartmentActivity("Midpoint", ActivityCategory.ACADEMIC, "Mid-Semester Project & Dissertation Progress Review"));
        activityRepo.add(new DepartmentActivity("SEA", ActivityCategory.ACADEMIC, "Semester End Assessment theory & practical viva"));
        activityRepo.add(new DepartmentActivity("Study Tour", ActivityCategory.ACADEMIC, "Industrial exposure visits to IT hubs (Bengaluru/Pune)"));
        activityRepo.add(new DepartmentActivity("Placements", ActivityCategory.ACADEMIC, "Campus recruitment drives by tier-1 MNCs and tech startups"));
        activityRepo.add(new DepartmentActivity("Workshops", ActivityCategory.ACADEMIC, "Hands-on national workshops on AI, Cloud, and Systems"));

        // Non-Academic Activities
        activityRepo.add(new DepartmentActivity("InfoFest", ActivityCategory.NON_ACADEMIC, "National-level flagship tech festival organized by DCST"));
        activityRepo.add(new DepartmentActivity("Plateaunia", ActivityCategory.NON_ACADEMIC, "Annual inter-department cultural festival of Goa University"));
        activityRepo.add(new DepartmentActivity("Play-on", ActivityCategory.NON_ACADEMIC, "Annual sports tournament of DCST covering indoor and outdoor events"));
        activityRepo.add(new DepartmentActivity("Beach Cleanup", ActivityCategory.NON_ACADEMIC, "Institutional Social Responsibility (ISR) environmental outreach"));

        GenericRepository.printGenericCollection(activityRepo.getAll(), "DCST Academic & Non-Academic Activity Registry");

        // Queue: Real-time Evaluation Pipeline
        Queue<String> assessmentQueue = new LinkedList<>();
        assessmentQueue.offer("MCA Lab Examination Batch-A");
        assessmentQueue.offer("M.Tech Dissertation Viva");
        assessmentQueue.offer("ISA Retest Evaluation");

        System.out.println("\n[Queue - FIFO Processing]");
        while (!assessmentQueue.isEmpty()) {
            System.out.println("Processing Queue Item: " + assessmentQueue.poll());
        }

        // Set: Distinct Courses Offered at DCST
        Set<String> uniquePrograms = new HashSet<>(Arrays.asList(
            "MCA (Master of Computer Applications)",
            "M.Tech in Computer Science & Technology",
            "M.Sc in Data Science",
            "Ph.D in Computer Science",
            "MCA (Master of Computer Applications)" // Duplicate will be dropped
        ));
        System.out.println("\n[Set - Unique Academic Offerings at DCST]");
        for (String prog : uniquePrograms) {
            System.out.println(" - " + prog);
        }

        // Map: Subject Mapping to Credit Weights
        Map<String, Integer> curriculumMap = new LinkedHashMap<>();
        curriculumMap.put("CST-101: Advanced Data Structures & Algorithms", 4);
        curriculumMap.put("CST-102: Operating Systems Architecture & Virtualization", 4);
        curriculumMap.put("CST-103: Machine Learning & Predictive Analytics", 4);
        curriculumMap.put("CST-104: Cloud Native Computing & DevOps", 3);
        curriculumMap.put("CST-105: Information Security & Network Defense", 3);

        System.out.println("\n[Map - Core Subjects and NIRF Credit Weights]");
        curriculumMap.forEach((subject, credits) -> System.out.println(String.format(" %-50s : %d Credits", subject, credits)));
        System.out.println();

        // --------------------------------------------------------------------
        // 9. EXCEPTION HANDLING
        // --------------------------------------------------------------------
        System.out.println(">>> 9. DEMONSTRATING EXCEPTION HANDLING (Try-Catch-Finally, Custom Exceptions)");
        try {
            validateEnrollmentCapacity("MCA", 85); // Exceeds threshold, triggers custom exception
        } catch (InvalidNIRFDataException e) {
            System.err.println("[Caught Custom Exception]: " + e.getMessage());
        } finally {
            System.out.println("[Finally Block Executed] NIRF audit safety validator cycle concluded.\n");
        }

        // --------------------------------------------------------------------
        // 10. REFLECTION, PERSISTENCE, I/O, CLONING
        // --------------------------------------------------------------------
        System.out.println(">>> 10. DEMONSTRATING CLONING (SHALLOW VS DEEP)");
        try {
            Faculty originalFaculty = new Faculty("F-03", "Dr. P. R.", 88.0, "Assistant Professor", "Cryptography", mainCampusAffiliation);
            Faculty shallowCloneFaculty = (Faculty) originalFaculty.shallowClone();
            Faculty deepCloneFaculty = (Faculty) originalFaculty.deepClone();

            // Mutate affiliation through shallow clone
            shallowCloneFaculty.affiliation.setDepartment("Modified Department");
            System.out.println("After mutating via shallow clone:");
            System.out.println(" - Original Object's Department: " + originalFaculty.affiliation.getDepartment());
            System.out.println(" - Deep Cloned Object's Department: " + deepCloneFaculty.affiliation.getDepartment() + " (Untouched due to deep clone)");

            // Reset back
            originalFaculty.affiliation.setDepartment("Department of Computer Science & Technology (DCST)");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        System.out.println("\n>>> 10. DEMONSTRATING REFLECTION & RTTI");
        try {
            Class<?> clazz = Class.forName("Faculty");
            System.out.println("Class Name (via RTTI): " + clazz.getName());
            System.out.println("Declared Fields:");
            for (Field f : clazz.getDeclaredFields()) {
                System.out.println(" - " + Modifier.toString(f.getModifiers()) + " " + f.getType().getSimpleName() + " " + f.getName());
            }

            System.out.println("Declared Methods:");
            for (Method m : clazz.getDeclaredMethods()) {
                System.out.println(" - " + m.getName() + " (Params: " + m.getParameterCount() + ")");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("\n>>> 10. DEMONSTRATING FILE I/O & OBJECT SERIALIZATION (PERSISTENCE)");
        String reportFilePath = "output" + File.separator + "nirf_report.txt";
        String serializedFilePath = "output" + File.separator + "staff_backup.ser";

        // Create output directory if not present
        new File("output").mkdirs();

        // File I/O
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFilePath))) {
            writer.write("GOA UNIVERSITY - DCST NIRF CONSOLIDATED AUDIT REPORT\n");
            writer.write("===================================================\n");
            writer.write("Total Registered Personnel: " + Member.getTotalMembers() + "\n");
            writer.write("Offered Programs: MCA, M.Tech, M.Sc, Ph.D\n");
            writer.write("Key Academic Events: ISA, Midpoint, SEA, Study Tours, Tech Placements\n");
            writer.write("Key Non-Academic Events: InfoFest, Plateaunia, Play-on, Social Outreach\n");
            System.out.println("[File I/O Success] Summary generated at: " + reportFilePath);
        } catch (IOException e) {
            System.err.println("File write failed: " + e.getMessage());
        }

        // Serialization Persistence
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializedFilePath))) {
            oos.writeObject(staff1);
            System.out.println("[Serialization Success] Staff state persisted to: " + serializedFilePath);
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
        }

        // Deserialization
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serializedFilePath))) {
            NonTeachingStaff restoredStaff = (NonTeachingStaff) ois.readObject();
            System.out.print("[Deserialization Success] Restored object: ");
            restoredStaff.printProfile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
        }

        System.out.println("\n========================================================================");
        System.out.println("  NIRF AUDIT PIPELINE COMPLETED SUCCESSFULLY");
        System.out.println("========================================================================");
    }

    // Helper validator to trigger custom exception
    private static void validateEnrollmentCapacity(String program, int studentCount) throws InvalidNIRFDataException {
        int maxCapacity = 60; // Max allowed per batch for NIRF compliance
        if (studentCount > maxCapacity) {
            throw new InvalidNIRFDataException("Intake violation for program '" + program + "': Registered " + 
                                               studentCount + " students exceeds sanctioned threshold of " + maxCapacity);
        }
    }
}