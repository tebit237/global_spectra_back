package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import iwomi.base.objects.ReportAttribute;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.User;

public interface UserRepository extends CrudRepository<User, Long>,JpaSpecificationExecutor{
	//@Query(value = "SELECT e.* FROM sause e WHERE e.sttus = ?1 ORDER BY e.lname Asc ",nativeQuery=true)
	List<User> findBySttus(String fich);
	User findById(Long id);

}
