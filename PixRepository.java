package br.unitins.topicos1.prancha.repository;
import java.util.List;
import br.unitins.topicos1.prancha.model.Pix;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PixRepository implements PanacheRepository<Pix> {

    public List<Pix> findByChave(String chave) {
        return list("chave", chave);
    }
    
}