package star.wars.resistance.network.socialnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import star.wars.resistance.network.socialnetwork.models.Item;
import star.wars.resistance.network.socialnetwork.models.Localizacao;
import star.wars.resistance.network.socialnetwork.models.Rebelde;
import star.wars.resistance.network.socialnetwork.repository.RebeldesRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RebeldesService {

    @Autowired
    RebeldesRepository rebeldesRepository;

    public Rebelde novo(Rebelde rebelde) {

        return rebeldesRepository.saveAndFlush(rebelde);
    }

    public Rebelde substitui(Rebelde r) {
        Rebelde rebelde = this.rebeldesRepository.findById(r.getId())
                .get();
        rebelde = r;
        return this.rebeldesRepository.saveAndFlush(rebelde);
    }

    public Rebelde atualizaNome(Rebelde r) {
        Rebelde rebelde = this.rebeldesRepository.findById(r.getId())
                .get();
        rebelde.setNome(r.getNome());
        return this.rebeldesRepository.saveAndFlush(rebelde);
    }

    public Rebelde atualizaGenero(Rebelde r) {
        Rebelde rebelde = this.rebeldesRepository.findById(r.getId())
                .get();
        rebelde.setGenero(r.getGenero());
        return this.rebeldesRepository.saveAndFlush(rebelde);
    }


    public Rebelde atualizarLocalizacao(Localizacao localizacao, Rebelde rebelde) {
        Rebelde r = this.rebeldesRepository.findById(rebelde.getId())
                .get();
        r.setLocalizacao(localizacao);
        rebelde.setLocalizacao(localizacao);

        return rebeldesRepository.saveAndFlush(r);
    }


    public List<Rebelde> todos() {
        return rebeldesRepository.findAll();
    }


    public Rebelde rebelde(Long id) {
        return rebeldesRepository.findById(id)
                .get();
    }

    public void toggleTraidor(Rebelde r) {
        r.toggleTraidor();
        this.rebeldesRepository.saveAndFlush(r);
    }

    public Rebelde negociarItems(Long r1, Long r2, Item item) {

        Rebelde rebelde1 = this.rebeldesRepository.getOne(r1);
        Rebelde rebelde2 = this.rebeldesRepository.getOne(r2);

        if (!rebelde1.isTraidor() && !rebelde2.isTraidor()) {
            rebelde1.getItems()
                    .setAgua(rebelde1.getItems()
                            .getAgua() - item.getAgua());
            rebelde2.getItems()
                    .setAgua(rebelde2.getItems()
                            .getAgua() + item.getAgua());

            rebelde1.getItems()
                    .setArma(rebelde1.getItems()
                            .getArma() - item.getArma());
            rebelde2.getItems()
                    .setArma(rebelde2.getItems()
                            .getArma() + item.getArma());

            rebelde1.getItems()
                    .setComida(rebelde1.getItems()
                            .getComida() - item.getComida());
            rebelde2.getItems()
                    .setComida(rebelde2.getItems()
                            .getComida() + item.getComida());

            rebelde1.getItems()
                    .setMunicao(rebelde1.getItems()
                            .getMunicao() - item.getMunicao());
            rebelde2.getItems()
                    .setMunicao(rebelde2.getItems()
                            .getMunicao() + item.getMunicao());
        }

        rebeldesRepository.save(rebelde1);
        rebeldesRepository.save(rebelde2);
        this.rebeldesRepository.flush();
        return rebelde1;
    }


    public Long pontosDoRebelde(Rebelde rebelde) {
        return rebelde.getItems() != null ?
                rebelde.getItems()
                        .getAgua() * Item.AGUA_PTS +
                        rebelde.getItems()
                                .getArma() * Item.ARMA_PTS +
                        rebelde.getItems()
                                .getComida() * Item.COMIDA_PTS +
                        rebelde.getItems()
                                .getMunicao() * Item.MUNICAO_PTS : 0l;
    }

    public double porcentagemDeTraidores() {
        List<Rebelde> todos = this.rebeldesRepository.findAll();

        List<Boolean> traidores = todos.stream()
                .map(traidor -> traidor.isTraidor())
                .filter(e -> e.booleanValue())
                .collect(Collectors.toList());

        return ((double) traidores.size() / (double) todos.size());

    }

    public double porcentagemDeRebeldes() {
        List<Rebelde> todos = this.rebeldesRepository.findAll();
        List<Boolean> rebeldes = todos.stream()
                .map(rebelde -> !rebelde.isTraidor())
                .filter(e -> e)
                .collect(Collectors.toList());

        return ((double) rebeldes.size() / (double) todos.size());

    }

    public Double mediaDeAgua() {
        List<Rebelde> rebeldes = this.rebeldesRepository.findAll();

        double aguas = rebeldes.stream()
                .map(rebelde -> rebelde.getItems()
                        .getAgua())
                .reduce(0l, (a, b) -> a + b)
                .doubleValue();

        return (aguas / (double) rebeldes.size());
    }

    public Double mediaDeArma() {
        List<Rebelde> rebeldes = this.rebeldesRepository.findAll();

        double armas = rebeldes.stream()
                .map(rebelde -> rebelde.getItems()
                        .getArma())
                .reduce(0l, (a, b) -> a + b)
                .doubleValue();

        return (armas / (double) rebeldes.size());
    }

    public Double mediaDeComida() {
        List<Rebelde> rebeldes = this.rebeldesRepository.findAll();

        double comidas = rebeldes.stream()
                .map(rebelde -> rebelde.getItems()
                        .getComida())
                .reduce(0l, (a, b) -> a + b)
                .doubleValue();

        return (comidas / (double) rebeldes.size());
    }

    public Double mediaDeMunicao() {
        List<Rebelde> rebeldes = this.rebeldesRepository.findAll();

        double municao = rebeldes.stream()
                .map(rebelde -> rebelde.getItems()
                        .getMunicao())
                .reduce(0l, (a, b) -> a + b)
                .doubleValue();

        return (municao / (double) rebeldes.size());
    }

    public long pontosPerdidos() {
        List<Rebelde> rebeldes = this.rebeldesRepository.findAll();

        long totalDePontosDosTraidores = 0l;

        for (Rebelde rebelde : rebeldes) {
            if (rebelde.isTraidor()) {
                totalDePontosDosTraidores += (rebelde.getItems()
                        .getAgua() * Item.AGUA_PTS)
                        + (rebelde.getItems()
                        .getArma() * Item.ARMA_PTS)
                        + (rebelde.getItems()
                        .getComida() * Item.COMIDA_PTS)
                        + (rebelde.getItems()
                        .getMunicao() * Item.MUNICAO_PTS);
            }
        }
//        long totalDePontosDosTraidores = rebeldes.stream()
//                .map(rebelde -> rebelde.isTraidor() ?
//                        rebelde.getItems() : new Item())
//                .map(item -> item.getAgua() * Item.AGUA_PTS + item.getArma() * Item.ARMA_PTS + item.getComida() * Item.COMIDA_PTS + item.getMunicao() * Item.MUNICAO_PTS)
//                .reduce(0l, (a, b) -> a + b);

        return totalDePontosDosTraidores;
    }
}
