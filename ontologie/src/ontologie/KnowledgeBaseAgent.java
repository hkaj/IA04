package ontologie;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import jade.core.Agent;

@SuppressWarnings("serial")
public class KnowledgeBaseAgent extends Agent {
	
	public static final String INIT_RULES_PATH = "ressources/foaf.n3";
	public static final String INIT_ASSERTIONS_PATH = "ressources/ABox";

	public KnowledgeBaseAgent(){
	}
	
	
	@Override
	public void setup(){
		//Initialisation du modèle de la base de connaissances
		m_model = ModelFactory.createDefaultModel();
		
		//Behaviour de remplissage de la base
		addBehaviour(new KBInitialiseKnowledgeBase(this, INIT_RULES_PATH, INIT_ASSERTIONS_PATH));
		
		//Behaviour de réception des messages
		addBehaviour(new KBReceiveMessageBehaviour(this));
	}
	
	
	//Getters & Setters
	public Model getModel() {return m_model;}
	public void setModel(Model model) {m_model = model;}
	
	//Members
	private Model m_model;
	
}
