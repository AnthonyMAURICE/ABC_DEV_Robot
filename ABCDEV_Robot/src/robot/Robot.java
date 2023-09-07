package robot;


public class Robot {
	// enum des différentes commandes pouvant se retrouver sur la manette finale
	enum Mouvement{
		AVANT,
		ARRIERE,
		GAUCHE,
		DROITE,
		SCANNER,
		SAISIR,
		LACHER,
		AGIR,
		DETRUIRE;
	}
	
	
	private String name, type;
	private int taille, posX, posY, direction;
	private boolean android, mobile, forward, left, scanZone = false, objectTaken;
	protected boolean powerOn;
	
	//constructeur par défaut
	public Robot() { 
		this.name = "Robot";
		this.type = "Androïde";
		this.taille = 180; //taille et déterminer si androïde ou pas sont inutilisés
		this.android = true; // mais pourraient être utile sur une évolution potentielle du programme
		this.mobile = true;
		this.powerOn = true;
		this.posX = 0;
		this.posY = 0;
		this.direction = 0;		
	}
	
	//constructeur avec paramètres
	public Robot(String _name, String _type, int _taille, boolean _androide, boolean _mobile, boolean _powerOn, int _posX, int _posY) {
		this.name = _name;
		this.type = _type;
		this.taille = _taille;
		this.android = _androide;
		this.mobile = _mobile;
		this.powerOn = _powerOn;
		this.posX = _posX;
		this.posY = _posY;
		this.direction = 0;
	}
	
	//Getters
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean getPower() {
		return this.powerOn;
	}
	
	public int getPosX() {
		return this.posX;
	}
	
	public int getPosY() {
		return this.posY;
	}
	
	//Setters
	// méthode qui met ou non le robot en marche suivant son état initial
	public void setPower() {
		if(this.powerOn) {
			this.powerOn = false;
		}else {
			this.powerOn = true;
		}
	}
	
	// méthode de mouvement, prenant l'enum déclaré en début de classe, si toutefois le robot est activé
	public boolean setMouvement(Mouvement _command) {
		if(this.powerOn) {
			Mouvement Ordre = _command;			
			switch(Ordre) {
				case AVANT:
					this.forward = true;
					this.setMove();
					break;
				case ARRIERE:
					this.forward = false;
					this.setMove();
					break;
				case GAUCHE:
					this.left = true;
					this.setDirection();
					break;
				case DROITE:
					this.left = false;
					this.setDirection();
					break;
				case SCANNER:
					this.scan();
					break;
				case SAISIR:
					this.takeObject();
					break;
				case LACHER:
					this.dropObject();
					break;
				case AGIR:
					this.agir();
					break;
				case DETRUIRE:
					this.destroyAllMankind();
					break;
			}
			return true;
		}else{
			System.out.println("Le robot n'est pas activé");
			return false;
		}
	}
	
	// détermine la direction après rotation, pour ensuite l'utiliser pour la phase de mouvement 
	public void setDirection() {
		if(this.left) { // calcul de la direction Y selon si la rotation est à gauche ou à droite
			if(this.direction >=90) {
				this.direction -= 90;
			}else {
				this.direction = 270;
			}
		}else {
			if(this.direction < 270 ) {
				this.direction += 90;
			}else {
				this.direction = 0;
			}
		}
	}
	
	// méthode de mouvement, basé sur sa direction (de 0 à 270) et si le robot avance ou recule
	public void setMove() { 
		if(this.mobile) {
			switch(this.direction) { // switch sur la valeur de la direction pour déterminer son déplacement sur les deux axes
			case 0: // ici axe X
				if(this.forward) {// booléen à true si le robot avance
					this.posX +=1;
				}else {
					this.posX -=1;
				}
				break;	
			case 90: // ici axe Y
				if(this.forward) {
					this.posY +=1;
				}else {
					this.posY -=1;
				}
				break;
			case 180: // axe X
				if(this.forward) {
					this.posX -=1;
				}else {
					this.posX +=1;
				}
				break;
			case 270: // axe Y
				if(this.forward) {
					this.posY -=1;
				}else {
					this.posY +=1;
				}
				break;
			}
			this.scanZone = false; // réinitialise le scan à false car changement de zone
		}else {
			System.out.println("Le robot n'est pas mobile");
		}
	}
	
	// scan du terrain préalable à la prise d'objets ou réalisation d'une recette (pour le robot de cuisine), reste à true tant qu'il n'y a pas changement de "zone"
	public void scan() {
		if(!this.scanZone) {
			this.scanZone = true;
		}
	}
	
	// ne retourne true que si un scan préalable a été effectué, si le robot ne porte pas déjà quelque chose et qu'il est mobile
	public boolean takeObject() {
		if(this.mobile && this.scanZone && !this.objectTaken) {
			this.objectTaken = true; // indique que le robot tient un objet
			return true;
		}else {
			return false;
		}
	}
	
	// ne retourne true que s'il a un objet à lâcher, et le lâche, sinon retourne false
	public boolean dropObject() {
		if(this.objectTaken) {
			this.objectTaken = false;
			return true;
		}else {
			return false;
		}
	}
	
	
	
	// méthode qui fait "agir" le robot, s'il est activé, pourrait accueillir d'autres cas suivant les types de robots créés
	public void agir() {
		String action;
		if(this.powerOn) {
			switch(this.type) {
				case "WarBot":
					action ="Le robot tire sur ses créateurs";
					break;
				case "Astromech":
					action = "Le robot répare le vaisseau";
					break;
				case "KitchenBot":
					if(this.scanZone) {
						action = "Le robot prépare un plat";
						this.scanZone = false; // une fois le plat prêt, réinitialise le scan à false, étant donné qu'il ne se déplace pas
					}else {
						action = "Avez-vous pensé à scanner les ingrédients avant ?";
					}
					break;
				default:
					action = "Le robot fait ce pourquoi il est conçu";
					break;
			}
		
		}else {
			action = "Le robot n'est pas activé";
		}
		System.out.println(action);

	}
	
	// méthode humoristique, mais néanmoins sérieuse...
	public boolean destroyAllMankind() {
		if(this.type.equals("WarBot")){
			System.out.println("Les cylons ont été créés par les humains... Ils ont évolué... Ils se sont rebellés...");
			return true;
		}else {
			System.out.println("Ce robot ne causera pas la chute de l'humanité. Du moins, en théorie...");
			return false;
		}
	}
	
}
