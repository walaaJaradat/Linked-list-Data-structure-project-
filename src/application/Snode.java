package application;

public class Snode {
    private int id;
    private String name;
    private double tawjihiGrade;
    private double placementTestGrade;
    private double admission_Mark;
    private String Major;
    private Snode next;
    private String rejectionReason; 
   Dnode dnode = new Dnode ();

    public Snode() {
		super();
	}
	public String getMajor() {
		return Major;
	}

	public void setMajor(String major) {
		Major = major;
	}

	public Snode(int id, String name, double tawjihiGrade, double placementTestGrade, String major) {
		super();
		this.id = id;
		this.name = name;
		this.tawjihiGrade = tawjihiGrade;
		this.placementTestGrade = placementTestGrade;
		Major = major;
	}


//	
	/**
	 * @return the rejectionReason
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}

	/**
	 * @param rejectionReason the rejectionReason to set
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Snode(String name) {
		super();
		this.name = name;
	}

	 public void setAdmissionMark(double admissionMark) {
	        this.admission_Mark = admissionMark; 
	    }
	 public double getAdmissionMark() {
	        return admission_Mark; 
	    }

	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setTawjihiGrade(double tawjihiGrade) {
		this.tawjihiGrade = tawjihiGrade;
	}

	public void setPlacementTestGrade(double placementTestGrade) {
		this.placementTestGrade = placementTestGrade;
	}

	public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTawjihiGrade() {
        return tawjihiGrade;
    }

    public double getPlacementTestGrade() {
        return placementTestGrade;
    }

    public Snode getNext() {
        return next;
    }

    public void setNext(Snode next) {
        this.next = next;
    }
    public double calculateAdmissionMark(double tawjihiWeight, double placementWeight) {
        return (tawjihiGrade * tawjihiWeight) + (placementTestGrade * placementWeight);
    }



	@Override
	public String toString() {
		return "Snode [id=" + id + ", name=" + name + ", tawjihiGrade=" + tawjihiGrade + ", placementTestGrade="
				+ placementTestGrade + ", admission_Mark=" + admission_Mark + ", Major=" + Major + ", next=" + next
				+ ", dnode=" + dnode + "]";
	}


}
