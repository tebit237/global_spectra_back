package iwomi.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import iwomi.base.objects.Conn;
import iwomi.base.objects.InAutorisationDecouvert;

public interface ConRepository extends JpaRepository<Conn, String> {
	List<Conn>  findByConn(String s);
}
