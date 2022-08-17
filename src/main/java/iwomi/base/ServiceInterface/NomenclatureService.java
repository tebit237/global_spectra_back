package iwomi.base.ServiceInterface;

import java.util.List;

import iwomi.base.objects.Nomenclature;

public interface NomenclatureService {

	List<Nomenclature> listAll();

	Nomenclature save(Nomenclature n);

	Nomenclature delete(long id);

	List<Nomenclature> constructfilterquery2(Nomenclature s);
}
