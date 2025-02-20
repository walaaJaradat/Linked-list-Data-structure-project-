package application;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Alert;

public class DLL {
    private Dnode First, Back;
    private int size;

        public Dnode getFirst() {
        return First;
    }

    public void setFirst(Dnode first) {
        First = first;
    }

    public Dnode getBack() {
        return Back;
    }
    public Dnode getLastDoubleNode() {
		if (First == null) {
			return null;
		}

		Dnode current = First;
		while (current.getNext() != null) {
			current = current.getNext();
		}

		return current;
	}

    public void setBack(Dnode back) {
        Back = back;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isEmpty() {
        return First == null;
    }
//******************************************************************************************************
    public void addFirst(String name) {
        Dnode newNode = new Dnode(name);
        if (isEmpty()) {
            First = Back = newNode;
            newNode.setNext(First);
            newNode.setPrev(Back);
        } else {
            newNode.setNext(First);
            newNode.setPrev(Back);
            First.setPrev(newNode);
            Back.setNext(newNode);
            First = newNode;
        }
        size++;
    }

//******************************************************************************************************
    public void addLast(String name) {
        Dnode newNode = new Dnode(name);
        if (isEmpty()) {
            First = Back = newNode;
            newNode.setNext(First);
            newNode.setPrev(Back);
        } else {
            Back.setNext(newNode);
            newNode.setPrev(Back);
            newNode.setNext(First);
            First.setPrev(newNode);
            Back = newNode;
        }
        size++;
    }
//******************************************************************************************************
    public boolean removeFirst() {
        if (isEmpty()) {
            return false;
        }
        if (size == 1) {
            First = Back = null;
        } else {
            Dnode temp = First;
            First = First.getNext();
            Back.setNext(First);
            First.setPrev(Back);
            temp.setNext(null);
            temp.setPrev(null);
        }
        size--;
        return true;
    }
//*****************************************************************************************************
    public boolean removeLast() {
        if (isEmpty()) {
            return false;
        }
        if (size == 1) {
            First = Back = null;
        } else {
            Dnode temp = Back;
            Back = Back.getPrev();
            Back.setNext(First);
            First.setPrev(Back);
            temp.setPrev(null);
            temp.setNext(null);
        }
        size--;
        return true;
    }
//*******************************************************************************************************
    public boolean remove(String name) {
        if (First == null) {
            return false; // The list is empty
        }
        if (First.getName().equals(name)) {
            return removeFirst();
        }
        if (Back.getName().equals(name)) {
            return removeLast();
        }
        Dnode current = First.getNext();
        while (current != First) { 
            if (current.getName().equals(name)) {
                Dnode previous = current.getPrev();
                previous.setNext(current.getNext());
                if (current.getNext() != First) {
                    current.getNext().setPrev(previous);
                } else {
                    Back = previous; 
                }
                current.setNext(null);
                current.setPrev(null);
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false; // Node not found
    }
    //************************************************************************************************
    public void add(Dnode newNode) {
        if (isEmpty()) {
            // If the list is empty, set the new node as both First and Back
            First = Back = newNode;
            newNode.setNext(First);
            newNode.setPrev(Back);
        } else {
            Dnode current = First;
            Dnode previous = null;

            // Find the correct insertion point
            do {
                previous = current;
                current = current.getNext();
            } while (current != First && current.getName().compareToIgnoreCase(newNode.getName()) < 0);

            // Insert the new node between previous and current
            previous.setNext(newNode);
            newNode.setPrev(previous);
            newNode.setNext(current);
            current.setPrev(newNode);

            if (current == First) {
                Back = newNode; 
            }
            if (previous == Back && newNode.getName().compareToIgnoreCase(First.getName()) < 0) {
                First = newNode; 
            }
        }
        size++;
    }


    //*************************************************************************************************
    public void update(String oldMajor, String newMajor, int newAcceptanceGrade, double newTawjihiWeight, double newPlacementTestWeight) {
        Dnode currentDnode = getFirst();

        while (currentDnode != null) {
            if (currentDnode.getName().equals(oldMajor)) {
                currentDnode.setName(newMajor);
                currentDnode.setAcceptanceGrade(newAcceptanceGrade);
                currentDnode.setPlacementWeight(newPlacementTestWeight);
                currentDnode.setTawjihiWeight(newTawjihiWeight);
                return;
            }
            currentDnode = currentDnode.getNext();
        }
    }
//*****************************************************************************************************
    public Dnode search(String major) {
        if (First == null) {
            return null; 
        }
        Dnode current = First;
        do {
            if (current.getName().equalsIgnoreCase(major.trim())) { 
                return current; 
            }
            current = current.getNext();
        } while (current != First); 

        return null; 
    } 
    //*******************************************************************************************
    // Get suggested majors based on student's admission mark
//    public DLL getSuggestedMajors(Snode student) {
//        DLL suggestedMajors = new DLL(); 
//        double admissionMark = student.getAdmissionMark();
//
//        if (First == null) return suggestedMajors; 
//
//        Dnode currentMajorNode = First;
//        do {
//            if (admissionMark >= currentMajorNode.getAcceptanceGrade()) {
//                suggestedMajors.add(currentMajorNode); // Add major node to suggested majors
//            }
//            currentMajorNode = currentMajorNode.getNext();
//        } while (currentMajorNode != First); 
//
//        return suggestedMajors;
//    }   
   
    //**********************************************************************************************
    public void saveMajorsToFile() {
        String filePath = "C:\\Users\\user\\Desktop\\project\\UpdatedMajors.txt";

        try (PrintWriter writer = new PrintWriter((filePath))) {
            Dnode currentMajor = First; // first node in the majors linked list

            if (currentMajor != null) {
                Dnode startNode = currentMajor; 
                do {
                    writer.println(currentMajor.getName() + " | " +
                                   currentMajor.getAcceptanceGrade() + " | " +
                                   currentMajor.getTawjihiWeight() + " | " +
                                   currentMajor.getPlacementWeight());
                    currentMajor = currentMajor.getNext(); // Move to the next major in the list
                } while (currentMajor != startNode); // Continue until we're back at the start node
            }
            
            showAlert2("Majors data saved successfully to file.");

        } catch (IOException e) {
            showAlert3("Error saving majors data: " + e.getMessage());
        }
    }


//******************************************************************************************************
    public void saveStudentsToFile() {
        String filePath = "C:\\Users\\user\\Desktop\\project\\UpdatedStudents.txt";

        try (PrintWriter writer = new PrintWriter(filePath)) {
            Dnode currentMajor = First; //  first major node

            // Check if the majors list is empty
            if (currentMajor == null) {
                showAlert3("No majors available to save students.");
                return; // Exit if there are no majors
            }

            // Loop through each major node
            do {
                Snode currentStudent = currentMajor.getStudentList().getFirst(); // Get the first student for the current major

                // Check if the current major has students
                if (currentStudent != null) {
                    do {
                        writer.println(currentStudent.getId() + " | " +
                                       currentStudent.getName() + " | " +
                                       currentStudent.getTawjihiGrade() + " | " +
                                       currentStudent.getPlacementTestGrade());
                        currentStudent = currentStudent.getNext(); // Move to the next student
                    } while (currentStudent != currentMajor.getStudentList().getFirst()); // Loop until back to the start of this list
                }

                currentMajor = currentMajor.getNext(); // Move to the next major
            } while (currentMajor != First); // Continue until we loop back to the first major

            showAlert2("Students data saved successfully to file.");

        } catch (IOException e) {
            showAlert3("Error saving students data: " + e.getMessage());
        }
    }

//*************************************************************************************************
    private void showAlert2(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText(message);
		alert.showAndWait();
	}
	private void showAlert3(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(message);
		alert.showAndWait();
	}
	//**********************************************************************************************
	public String calculateAllStatistics() {
        int totalAccepted = 0;
        int totalRejected = 0;

        Dnode currentMajor = First;

        if (currentMajor == null) {
            return "No data available in the list.";
        }

        do {
            totalAccepted += currentMajor.getAcceptedCount();
            totalRejected += currentMajor.getRejectedCount();
            currentMajor = currentMajor.getNext();
        } while (currentMajor != First);

        int totalEvaluated = totalAccepted + totalRejected;
        double acceptanceRate = totalEvaluated == 0 ? 0 : (totalAccepted / (double) totalEvaluated) * 100;

        StringBuilder statsBuilder = new StringBuilder();
        statsBuilder.append("Total Accepted Students: ").append(totalAccepted).append("\n")
                    .append("Total Rejected Students: ").append(totalRejected).append("\n")
                    .append("Total Evaluated Students: ").append(totalEvaluated).append("\n")
                    .append("Acceptance Rate: ").append(String.format("%.2f", acceptanceRate)).append("%\n");

        return statsBuilder.toString();
    }
//****************************************************************************************************
    // Method to calculate statistics for a specific major
	public String calculateStatisticsForEachMajor(String selectedMajor) {
	    Dnode currentMajor = search(selectedMajor);

	    if (currentMajor == null) {
	        return "Major not found.";
	    }

	    int acceptedCount = currentMajor.getAcceptedCount();
	    int rejectedCount = currentMajor.getRejectedCount();

	    StringBuilder statsBuilder = new StringBuilder();
	    statsBuilder.append("\n--- Statistics for Major: ").append(currentMajor.getName()).append(" ---\n")
	                .append("Total Accepted: ").append(acceptedCount).append("\n")
	                .append("Total Rejected: ").append(rejectedCount).append("\n");

	    if (currentMajor.getStudentList() == null || currentMajor.getStudentList().getFirst() == null) {
	        return "No students found for this major.";
	    }

	    // Iterate through students to append rejection reasons
	    Snode student = currentMajor.getStudentList().getFirst();
	    do {
	        if (student != null) {
	            double admissionMark = student.getAdmissionMark();
	            if (admissionMark < currentMajor.getAcceptanceGrade()) {
	                String rejectionReason = getRejectionReason(student, currentMajor);
	                statsBuilder.append("Student ID: ").append(student.getId())
	                            .append(" Rejection Reason: ").append(rejectionReason)
	                            .append("\n");
	            }
	            student = student.getNext();
	        }
	    } while (student != null && student != currentMajor.getStudentList().getFirst());

	    
	    return statsBuilder.toString();
	}
//***************************************************************************************************
    // Method to get top N students for a specific major
    public String getTopNStudentsForMajor(int topN, String selectedMajor) {
        Dnode currentMajor = search(selectedMajor);

        if (currentMajor == null) {
            return "Major not found.";
        }

        StringBuilder result = new StringBuilder("Top " + topN + " Students in Major: " + selectedMajor + "\n");
        SLL sortedStudents = sortStudentsByAdmissionMark(currentMajor.getStudentList(), currentMajor);
        Snode student = sortedStudents.getFirst();
        int count = 0;

        if (student != null) {
            do {
                if (student.getAdmissionMark() >= currentMajor.getAcceptanceGrade()) {
                    result.append("Student ID: ").append(student.getId())
                          .append(", Name: ").append(student.getName())
                          .append(", Admission Mark: ").append(String.format("%.2f", student.getAdmissionMark()))
                          .append("\n");
                    count++;
                    if (count >= topN) break;
                }
                student = student.getNext();
            } while (student != sortedStudents.getFirst());
        }

        if (count == 0) {
            result.append("No accepted students found in this major.\n");
        }

        return result.toString();
    }
//*****************************************************************************************************
    // Method to get rejection reason
    private String getRejectionReason(Snode student, Dnode major) {
        double studentTawjihiDecimal = convertGradeToDecimal(student.getTawjihiGrade());
        double studentPlacementDecimal = convertGradeToDecimal(student.getPlacementTestGrade());
        
        double majorTawjihiWeightDecimal = major.getTawjihiWeight();
        double majorPlacementWeightDecimal = major.getPlacementWeight(); 

        if (studentTawjihiDecimal < majorTawjihiWeightDecimal && studentPlacementDecimal < majorPlacementWeightDecimal) {
            return "Tawjihi grade & Placement Test grade are too low";
        } else if (studentTawjihiDecimal < majorTawjihiWeightDecimal) {
            return "Tawjihi grade too low";
        } else if (studentPlacementDecimal < majorPlacementWeightDecimal) {
            return "Placement Test grade too low";
        } else {
            return "Unknown"; 
        }
    }

    private double convertGradeToDecimal(double grade) {
        // Assuming the maximum grade is 100 
        final double maxGrade = 100.0;
        return grade / maxGrade;
    }

//******************************************************************************************************
    // Method to sort students by admission mark in descending order
    private SLL sortStudentsByAdmissionMark(SLL studentList, Dnode majorNode) {
        SLL sortedList = new SLL();
        Snode current = studentList.getFirst();

        if (studentList == null || current == null) {
            return sortedList; 
        }

        do {
            if (current != null) { 
                Snode next = current.getNext();
                insertInSortedOrder(sortedList, current, majorNode);
                current = next;
            } else {
                showAlert2("Warning: Current student is null.");
                break; 
            }
        } while (current != null && current != studentList.getFirst());

        return sortedList;
    }

//******************************************************************************************************
    // Method to insert student in sorted order
    private void insertInSortedOrder(SLL sortedList, Snode newStudent, Dnode majorNode) {
        if (sortedList.getFirst() == null || newStudent.getAdmissionMark() > sortedList.getFirst().getAdmissionMark()) {
            newStudent.setNext(sortedList.getFirst());
            sortedList.setFirst(newStudent); // Update first
        } else {
            Snode current = sortedList.getFirst();
            while (current.getNext() != null && current.getNext().getAdmissionMark() >= newStudent.getAdmissionMark()) {
                current = current.getNext();
            }
            newStudent.setNext(current.getNext());
            current.setNext(newStudent);
        }
    }

}
