//package iwomi.base.services;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FichierSpectraServiceImpl implements FichierSpectraService{
//	@Autowired
//	private FichierSpectraRepository fichierSpectraRepository;
//	public FichierSpectraServiceImpl(FichierSpectraRepository fichierSpectraRepository) {
//		super();
//		this.fichierSpectraRepository = fichierSpectraRepository;
//	}
//	@Autowired
//	private FichierSpectraRepository fichierSpectraRepository;
//	 @Override
//	    public List<FichierSpectra> listAll() {
//		 List<FichierSpectra> fichierSpectras = new ArrayList<>();
//	        return  fichierSpectras;
//	       } 
//	 
//	 
//        public FichierSpectra delete(Long id) {
//                FichierSpectra fichier = new FichierSpectra();
//                fichier.setId(id);
//                fichier.setDele(1);
//                return fichierSpectraRepository.save(fichier);
//
//        }
//
//}
