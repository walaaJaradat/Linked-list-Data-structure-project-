package application;

public class Dnode {
    private String name;
    private int acceptanceGrade;
    private double tawjihiWeight;
    private double placementWeight;
    private SLL studentList; 
    private Dnode next;
    private Dnode prev;

    public Dnode() {
        this.studentList = new SLL();
    }

    public Dnode(String name) {
        this.name = name;
        this.studentList = new SLL();
    }

    public Dnode(String name, double tawjihiWeight, double placementWeight) {
        this.name = name;
        this.tawjihiWeight = tawjihiWeight;
        this.placementWeight = placementWeight;
        this.studentList = new SLL();
    }

    public Dnode(String name, int acceptanceGrade, double tawjihiWeight, double placementWeight) {
        this.name = name;
        this.acceptanceGrade = acceptanceGrade;
        this.tawjihiWeight = tawjihiWeight;
        this.placementWeight = placementWeight;
        this.studentList = new SLL();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAcceptanceGrade() {
        return acceptanceGrade;
    }

    public void setAcceptanceGrade(int acceptanceGrade) {
        this.acceptanceGrade = acceptanceGrade;
    }

    public double getTawjihiWeight() {
        return tawjihiWeight;
    }

    public void setTawjihiWeight(double tawjihiWeight) {
        this.tawjihiWeight = tawjihiWeight;
    }

    public double getPlacementWeight() {
        return placementWeight;
    }

    public void setPlacementWeight(double placementWeight) {
        this.placementWeight = placementWeight;
    }

    public SLL getStudentList() {
        if (studentList == null) {
            studentList = new SLL();
        }
        return studentList;
    }

    public void setStudentList(SLL studentList) {
        this.studentList = studentList;
    }

    public Dnode getNext() {
        return next;
    }

    public void setNext(Dnode next) {
        this.next = next;
    }

    public Dnode getPrev() {
        return prev;
    }

    public void setPrev(Dnode prev) {
        this.prev = prev;
    }

    // Method to check if a student is accepted in this major
    public boolean isAcceptedInMajor(Snode student) {
        if (student == null) {
            System.out.println("Warning: Attempted to check acceptance for a null student."); 
            return false; 
        }
        double admissionMark = student.calculateAdmissionMark(this.tawjihiWeight, this.placementWeight);
        return admissionMark >= this.acceptanceGrade;
    }


 // Method to calculate the count of accepted students
    public int getAcceptedCount() {
        int acceptedCount = 0;
        Snode current = studentList.getFirst();
        if (current == null) return acceptedCount; 

        do {
            if (isAcceptedInMajor(current)) {
                acceptedCount++;
            }
            current = current.getNext();
            if (current == null) {
                break; 
            }
        } while (current != studentList.getFirst());

        return acceptedCount;
    }

    // Method to calculate the count of Rejected students
    public int getRejectedCount() {
        int rejectedCount = 0;
        Snode current = studentList.getFirst();
        if (current == null) return rejectedCount; 

        do {
            if (current != null) { 
                if (!isAcceptedInMajor(current)) {
                    rejectedCount++;
                }
                current = current.getNext();
            } else {
                System.out.println("Warning: Current student is null."); 
                break;
            }
        } while (current != studentList.getFirst());

        return rejectedCount;
    }



    @Override
    public String toString() {
        return "Dnode [name=" + name + ", acceptanceGrade=" + acceptanceGrade + 
               ", tawjihiWeight=" + tawjihiWeight + ", placementWeight=" + placementWeight + "]";
    }
}
