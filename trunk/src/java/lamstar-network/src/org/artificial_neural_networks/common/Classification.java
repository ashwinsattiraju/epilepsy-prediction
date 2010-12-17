/**
 * 
 */
package org.artificial_neural_networks.common;

/**
 * @author  riccardo  This abstract class represents the abstraction of the concept class. It is   used to determine the belonging of a DatasetElement to a particular class.
 */
public class Classification implements Comparable<Classification>{
	/**
	 * Name of the class
	 * @uml.property  name="className"
	 */
	private String className;
	/**
	 * Unique value of this class
	 * @uml.property  name="classID"
	 */
	private long classID;
	
	/**
	 * Default constructor.
	 * @param pClassName a human-readable alias used to distinguish classes
	 * @param pClassID a machine-readable alias used to distinguish classes
	 */
	public Classification(String pClassName, long pClassID) {
		className = pClassName;
		classID = pClassID;
	}

	/**
	 * @return  the className
	 * @uml.property  name="className"
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className  the className to set
	 * @uml.property  name="className"
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return  the classID
	 * @uml.property  name="classID"
	 */
	public long getClassID() {
		return classID;
	}

	/**
	 * @param classID  the classID to set
	 * @uml.property  name="classID"
	 */
	public void setClassID(long classID) {
		this.classID = classID;
	}
	
	/**
	 * @param compareTo the Classification to compare
	 * @return true iif the IDs are equal, false otherwise
	 */
	public boolean equals(Classification compareTo) {
		return compareTo.getClassID() == classID;
	}
	/**
	 * @return a hard copy of this object.
	 */
	public Classification clone() {
		return new Classification(className, classID);
	}

	@Override
	public int compareTo(Classification o) {
		if (equals(o))
			return 0;
		else if (this.getClassID() > o.getClassID())
			return 1;
		else
			return -1;			
	}
	
	@Override
	public String toString() {
		return className.toString() + "-" + String.valueOf(classID);
	}
}
