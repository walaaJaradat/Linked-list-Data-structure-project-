package application;


public class SLL {
    private int size;
    private Snode first, back;


   
    public SLL() {
		super();
	}

	public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public Snode getFirst() {
        return first;
    }

    public void setFirst(Snode first) {
        this.first = first;
    }

    public Snode getBack() {
        return back;
    }

    public void setBack(Snode back) {
        this.back = back;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public void addFirst(Snode newStudent) {
        if (isEmpty()) {
            first = back = newStudent; 
            newStudent.setNext(first);
        } else {
            newStudent.setNext(first);
            back.setNext(newStudent);
            first = newStudent;
        }
        size++;
    }
    public void addLast(Snode newStudent) {
        if (isEmpty()) {
            first = back = newStudent;
            newStudent.setNext(first);
        } else {
            back.setNext(newStudent);
            newStudent.setNext(first);
            back = newStudent;
        }
        size++;
    }
    //*********************************************************************************************
    public void add(Snode newStudent, Dnode majorNode) {
        if (majorNode == null) {
            throw new IllegalArgumentException("majorNode cannot be null");
        }

        double tawjihiWeight = majorNode.getTawjihiWeight();
        double placementWeight = majorNode.getPlacementWeight();
        double admissionMark = ((newStudent.getTawjihiGrade() * tawjihiWeight) + (newStudent.getPlacementTestGrade() * placementWeight));

        newStudent.setAdmissionMark(admissionMark);

        if (first == null) {
            first = newStudent;
            back = newStudent;
            first.setNext(first); 
        } else {
            Snode current = first;
            Snode temp = null;

            do {
                if (admissionMark >= current.getAdmissionMark()) { 
                    if (temp == null) {
                        newStudent.setNext(first);
                        first = newStudent;
                        back.setNext(first); 
                    } else {
                        temp.setNext(newStudent);
                        newStudent.setNext(current);
                    }
                    size++;
                    return;
                }

                temp = current;
                current = current.getNext();
            } while (current != first); 

            back.setNext(newStudent);
            newStudent.setNext(first);
            back = newStudent; 
        }

        size++; 
    }
//******************************************************************************************************

    public Snode removeFirst() {
        if (isEmpty()) {
            return null;
        }

        Snode removedNode = first;
        if (first == back) {
            first = back = null;
        } else {
            first = first.getNext();
            back.setNext(first);
        }
        size--;
        return removedNode;
    }
//******************************************************************************************************
    public Snode removeLast() {
        if (isEmpty()) {
            return null;
        }

        Snode removedNode = back;
        if (first == back) {
            first = back = null;
        } else {
            Snode current = first;
            while (current.getNext() != back) {
                current = current.getNext();
            }
            current.setNext(first);
            back = current;
        }
        size--;
        return removedNode;
    }

//*****************************************************************************************************   
    public Snode search(int studentId) {
        if (first == null) {
            return null;
        }

        Snode current = first;
        do {
            if (current.getId()==(studentId)) {
                return current;
            }
            current = current.getNext();
        } while (current != first);

        return null;
    }
    //**************************************************************************************************
    public Snode remove(Snode snode) {
        if (isEmpty()) {
            return null;
        }

        if (first.getId()==(snode.getId())) {
            return removeFirst();
        }

        Snode current = first;
        while (current.getNext() != first && 
                !(current.getNext().getId()==(snode.getId()))) {
            current = current.getNext();
        }

        if (current.getNext() == first) {
            return null;
        }

        Snode removedNode = current.getNext();
        current.setNext(removedNode.getNext());

        if (removedNode == back) {
            back = current;
        }

        size--;
        return removedNode;
    }

}
