package com.androidtutorialpoint.reto8.Model;

public class Contact {
	private long conId;
	private String name;
	private String url;
	private String phone;
	private String email;
	private String description;
	private int isConsulting;
	private int isDevelopment;
	private int isSoftwareFactory;

	public Contact(long conId, String name, String url, String phone, String email, String description,
                    int isConsulting, int isDevelopment, int isSoftwareFactory) {
		this.conId = conId;
		this.name = name;
		this.url = url;
		this.phone = phone;
		this.email = email;
		this.description = description;
        this.isConsulting = isConsulting;
        this.isDevelopment = isDevelopment;
        this.isSoftwareFactory = isSoftwareFactory;
	}

	public Contact() {

	}

	public long getConId() {
		return conId;
	}

	public void setConId(long conId) {
		this.conId = conId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public int isConsulting() {
        return isConsulting;
    }

    public void setConsulting(int isConsulting) {
        this.isConsulting = isConsulting;
    }

    public int isDevelopment() {
        return isDevelopment;
    }

    public void setDevelopment(int isDevelopment) {
        this.isDevelopment = isDevelopment;
    }

    public int isSoftwareFactory() {
        return isSoftwareFactory;
    }

    public void setSoftwareFactory(int isSoftwareFactory) {
        this.isSoftwareFactory = isSoftwareFactory;
    }

    public String toString(){
        String classification = "";

        if(isConsulting() == 1) classification += "Consultoría";
        if(isDevelopment() == 1) {
            if(!classification.isEmpty()) classification += ", ";
            classification += "Desarrollo a la medida";
        }
        if(isSoftwareFactory() == 1) {
            if(!classification.isEmpty()) classification += ", ";
            classification += "Fábrica de software";
        }

		return "ID de contacto: "+ getConId()+ "\n" +
				getName() + "\n" +
                "(" + classification + ")" + "\n" +
				getUrl() + "\n" +
                getEmail() + "\n" +
                "Teléfono: " + getPhone() + "\n" +
				getDescription();
	}
}
