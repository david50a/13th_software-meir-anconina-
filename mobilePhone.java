public class mobilePhone{
    // define the features of the class
    public String brand,model;
    public double price;
    // create a constractor function
    public mobilePhone(String brard,String model,double price){
        this.brand=brard.length()>0?brard:"enpty name";
        this.model=model.length()>0?model:"empty name";
        this.price=price>0?price:0;
    }
    // prints the details of the phone
    public void printDetails(){
        // the function prints the features of the phone
        System.err.println("Name of brand: "+this.brand);
        System.err.println("Name of model: "+this.model);
        System.err.println("Price: "+(this.price>0?this.price:"NULL"));
    }
    //the function make a discount for the price of the phone
    public void discount(double precent){
        //checks if the price is not null
        if (this.price>0){
            // checks if the precent is between 0-100
            if(precent>0 && precent<100){
                //the function multiply this.price by the precent given
                this.price*=(100-precent)/100;
            }
            else{System.err.println("problem in the precent of the discount");}
        }
        else{System.err.println("Problem in the price");}
    }
    public static void main(String[] args) {
        //create two mobilePhone's objects 
        mobilePhone m1=new mobilePhone("fff", "tttt", 50);
        mobilePhone m2=new mobilePhone("rrr", "www", 40);
        m1.printDetails();
        m2.printDetails();
        m1.discount(51.3);
        m2.discount(6);
        m1.printDetails();
        m2.printDetails();
        
    }

}