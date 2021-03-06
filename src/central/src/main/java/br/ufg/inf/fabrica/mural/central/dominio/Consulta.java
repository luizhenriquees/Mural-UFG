/*
 * ====================================================================
 * Licença da Fábrica de Software (UFG)
 *
 * Copyright (c) 2014 Fábrica de Software
 * Instituto de Informática (Universidade Federal de Goiás)
 * Todos os direitos reservados.
 *
 * Redistribuição e uso, seja dos fontes ou do formato binário
 * correspondente, com ou sem modificação, são permitidos desde que
 * as seguintes condições sejam atendidas:
 *
 * 1. Redistribuição do código fonte deve conter esta nota, em sua
 *    totalidade, ou seja, a nota de copyright acima, as condições
 *    e a observação sobre garantia abaixo.
 *
 * 2. Redistribuição no formato binário deve reproduzir o conteúdo
 *    desta nota, em sua totalidade, ou seja, o copyright acima,
 *    esta lista de condições e a observação abaixo na documentação
 *    e/ou materiais fornecidos com a distribuição.
 *
 * 3. A documentação fornecida com a redistribuição,
 *    qualquer que seja esta, deve incluir o seguinte
 *    texto, entre aspas:
 *       "Este produto inclui software desenvolvido pela Fábrica
 *       de Software do Instituto de Informática (UFG)."
 *
 * 4. Os nomes Fábrica de Software, Instituto de Informática e UFG
 *    não podem ser empregados para endoçar ou promover produtos
 *    derivados do presente software sem a explícita permissão
 *    por escrito.
 *
 * 5. Produtos derivados deste software não podem ser chamados
 *    "Fábrica de Software", "Instituto de Informática", "UFG",
 *    "Universidade Federal de Goiás" ou contê-los em seus nomes,
 *    sem permissão por escrito.
 *
 * ESTE SOFTWARE É FORNECIDO "COMO ESTÁ". NENHUMA GARANTIA É FORNECIDA,
 * EXPLÍCITA OU NÃO. NÃO SE AFIRMA QUE O PRESENTE SOFTWARE
 * É ADEQUADO PARA QUALQUER QUE SEJA O USO. DE FATO, CABE AO
 * INTERESSADO E/OU USUÁRIO DO PRESENTE SOFTWARE, IMEDIATO OU NÃO,
 * ESTA AVALIAÇÃO E A CONSEQUÊNCIA QUE O USO DELE OCASIONAR. QUALQUER
 * DANO QUE DESTE SOFTWARE DERIVAR DEVE SER ATRIBUÍDO AO INTERESSADO
 * E/OU USUÁRIO DESTE SOFTWARE.
 * ====================================================================
 *
 * Este software é resultado do trabalho de voluntários, estudantes e
 * professores, ao realizar atividades no âmbito da Fábrica de Software
 * do Instituto de Informática (UFG). Consulte <http://fs.inf.ufg.br>
 * para detalhes.
 */

package br.ufg.inf.fabrica.mural.central.dominio;


import br.ufg.inf.fabrica.mural.central.persistencia.PublicacaoDAOImpl;

import java.util.Collection;
import java.util.Date;

/**
 * Representa uma Solicitação de busca na lista de informações publicadas.
 */
public class Consulta extends Solicitacao {

    private Collection<Publicacao> publicacoesVigentes = null;
    private String termo;
    private Date dataInicio;
    private Date dataFim;

    public Consulta(String termo, Date dataInicio, Date dataFim) {
        this.termo = termo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    /**
     * Confere se o termo não é nulo e se a data de início da vigência
     * não é posterior à data de fim.
     * @param termo representa o termo a ser pesquisado na consulta
     * @param dataInicio Data inicial da consulta a ser realizada
     * @param dataFim Data final da consulta a ser realizada
     * @return true se a solicitação é válida e false caso contrário
     */
    private boolean validarSolicitacao(String termo, Date dataInicio, Date dataFim){
        if (termo != null){
            return dataFim.compareTo(dataInicio) >= 0;
        }
        return false;
    }

    /**
     * Realiza a consulta de publicações.
     * @return Coleção que representa o conjunto de Publicacao obtida a partir da Consulta criada
     * ou null caso não seja possível realizar a Consulta
     */
    public Collection executaConsulta(){
        setDataAbertura(new Date());
        if (validarSolicitacao(termo, dataInicio, dataFim)){
            setEstado("Aceita");
            PublicacaoDAOImpl publicacaoDAO = new PublicacaoDAOImpl();
            publicacoesVigentes = publicacaoDAO.consultarPublicacoes(termo, dataInicio, dataFim);
            if (publicacoesVigentes.isEmpty()){
                setDescricaoEstado("Nenhum resultado encontrado!");
            } else {
                setDescricaoEstado("Solicitação tratada com sucesso");
            }
            return publicacoesVigentes;
        } else {
            setEstado("Negada");
            setDescricaoEstado("Solicitação recusada por inconsistência de informação.");
        }
        return publicacoesVigentes;
    }

    public Collection getPublicacoesVigentes() {
        return publicacoesVigentes;
    }

    public void setPublicacoesVigentes(Collection publicacoesVigentes) {
        this.publicacoesVigentes = publicacoesVigentes;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
