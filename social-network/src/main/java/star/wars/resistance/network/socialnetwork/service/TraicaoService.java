package star.wars.resistance.network.socialnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import star.wars.resistance.network.socialnetwork.models.Rebelde;
import star.wars.resistance.network.socialnetwork.models.Traicao;
import star.wars.resistance.network.socialnetwork.repository.RebeldesRepository;
import star.wars.resistance.network.socialnetwork.repository.TraicaoRepository;

import java.util.List;

@Service
public class TraicaoService {

    @Autowired
    TraicaoRepository traicaoRepository;

    @Autowired
    RebeldesRepository rebeldesRepository;

    public Traicao novo(Traicao traicao) {
        Traicao t = traicaoRepository.save(traicao);

        List<Traicao> traidores = traicaoRepository.findByTraido(traicao.getTraido());

        if (traidores.size() >= 3) {
            Rebelde rebelde = rebeldesRepository.findById(traicao.getTraido().getId()).get();
            rebelde.setTraidor(true);
            rebeldesRepository.saveAndFlush(rebelde);
        }

        traicaoRepository.flush();
        return t;
    }

    public List<Traicao> todos(){
        return traicaoRepository.findAll();
    }

    public TraicaoService() {
    }
}