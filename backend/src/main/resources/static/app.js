const api = '/apis';
const form = document.querySelector('#vaga-form');
const feedback = document.querySelector('#feedback');
const tbody = document.querySelector('#vagas-body');
const buscaVagas = document.querySelector('#busca-vagas');
const resultadoBusca = document.querySelector('#resultado-busca');
const vagaTooltip = document.querySelector('#vaga-tooltip');
let tooltipAtivo = null;
let vagas = [];
let empresas = [];
let cargos = [];

const fields = {
    id: document.querySelector('#vaga-id'),
    registro: document.querySelector('#registro'),
    empresa: document.querySelector('#empresa'),
    cargo: document.querySelector('#cargo'),
    cidade: document.querySelector('#cidade'),
    estado: document.querySelector('#estado'),
    regime: document.querySelector('#regime'),
    jornada: document.querySelector('#jornada'),
    remuneracao: document.querySelector('#remuneracao'),
    formacao: document.querySelector('#formacao'),
    requisitos: document.querySelector('#requisitos'),
    conhecimentos: document.querySelector('#conhecimentos')
};

async function request(url, options) {
    const response = await fetch(url, options);
    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        const details = error.campos ? `: ${Object.values(error.campos).join(', ')}` : '';
        throw new Error((error.mensagem || `Erro HTTP ${response.status}`) + details);
    }
    return response.status === 204 ? null : response.json();
}

async function carregarCatalogos() {
    [empresas, cargos] = await Promise.all([
        request(`${api}/empresa/get-all-empresas`),
        request(`${api}/cargo/get-all-cargos`)
    ]);
    fields.empresa.innerHTML = empresas.map(item =>
        `<option value="${escapeHtml(item.nome_fantasia)}">${escapeHtml(item.nome_fantasia)}</option>`).join('');
    fields.cargo.innerHTML = cargos.map(item =>
        `<option value="${escapeHtml(item.nome)}">${escapeHtml(item.nome)}</option>`).join('');
}

async function carregarVagas() {
    vagas = await request(`${api}/vagas/get-all`);
    renderizarVagas();
}

function renderizarVagas() {
    const vagasFiltradas = filtrarVagas();
    const empty = document.querySelector('#empty');
    empty.textContent = vagas.length === 0
        ? 'Nenhuma vaga cadastrada.'
        : 'Nenhuma vaga encontrada para esta busca.';
    empty.classList.toggle('hidden', vagasFiltradas.length > 0);
    resultadoBusca.textContent = `${vagasFiltradas.length} de ${vagas.length} vaga(s)`;

    tbody.innerHTML = vagasFiltradas.map(vaga => `
        <tr>
            <td><strong>${escapeHtml(vaga.cargo)}</strong></td>
            <td>${escapeHtml(vaga.empresa.nome_fantasia)}</td>
            <td>${escapeHtml(vaga.cidade)}/${escapeHtml(vaga.estado)}</td>
            <td>${escapeHtml(vaga.regime)}</td>
            <td>${escapeHtml(vaga.jornada_trabalho)}</td>
            <td class="details-column">
                <div class="details-thumbnails">
                    ${criarThumbnail('Formação', vaga.formacao)}
                    ${criarThumbnail('Requisitos', vaga.pre_requisitos)}
                    ${criarThumbnail('Conhecimentos', vaga.conhecimentos_requeridos)}
                </div>
            </td>
            <td>${escapeHtml(vaga.remuneracao)}</td>
            <td class="actions-column">
                <div class="actions-buttons">
                    <button class="button ghost small" data-edit="${escapeHtml(vaga.registro)}">Editar</button>
                    <button class="button danger small" data-delete="${escapeHtml(vaga.registro)}">Excluir</button>
                </div>
            </td>
        </tr>`).join('');
}

function criarThumbnail(titulo, conteudo) {
    return `<button type="button" class="info-chip"
                    aria-label="Exibir ${escapeHtml(titulo)}"
                    data-tooltip-title="${escapeHtml(titulo)}"
                    data-tooltip="${escapeHtml(conteudo)}">${escapeHtml(titulo)}</button>`;
}

function mostrarTooltip(elemento) {
    tooltipAtivo = elemento;
    vagaTooltip.replaceChildren();

    const titulo = document.createElement('strong');
    titulo.textContent = elemento.dataset.tooltipTitle;
    const conteudo = document.createElement('span');
    conteudo.textContent = elemento.dataset.tooltip;
    vagaTooltip.append(titulo, conteudo);
    vagaTooltip.classList.add('visible');
    vagaTooltip.setAttribute('aria-hidden', 'false');
    elemento.setAttribute('aria-describedby', 'vaga-tooltip');

    const alvo = elemento.getBoundingClientRect();
    const tooltip = vagaTooltip.getBoundingClientRect();
    const margem = 12;
    let left = alvo.left + (alvo.width - tooltip.width) / 2;
    left = Math.max(margem, Math.min(left, window.innerWidth - tooltip.width - margem));
    let top = alvo.bottom + 8;
    if (top + tooltip.height > window.innerHeight - margem) {
        top = alvo.top - tooltip.height - 8;
    }
    vagaTooltip.style.left = `${left}px`;
    vagaTooltip.style.top = `${Math.max(margem, top)}px`;
}

function esconderTooltip() {
    if (tooltipAtivo) tooltipAtivo.removeAttribute('aria-describedby');
    tooltipAtivo = null;
    vagaTooltip.classList.remove('visible');
    vagaTooltip.setAttribute('aria-hidden', 'true');
}

function filtrarVagas() {
    const termo = normalizar(buscaVagas.value);
    if (!termo) return vagas;

    return vagas.filter(vaga => [
        vaga.registro,
        vaga.cargo,
        vaga.empresa?.nome_fantasia,
        vaga.empresa?.razao_social,
        vaga.cidade,
        vaga.estado,
        vaga.regime,
        vaga.jornada_trabalho,
        vaga.formacao,
        vaga.pre_requisitos,
        vaga.conhecimentos_requeridos,
        vaga.remuneracao
    ].some(valor => normalizar(valor).includes(termo)));
}

function normalizar(value = '') {
    return String(value)
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase()
        .trim();
}

form.addEventListener('submit', async event => {
    event.preventDefault();
    setFeedback('Salvando...', '');
    const empresaSelecionada = empresas.find(item => item.nome_fantasia === fields.empresa.value);
    const body = {
        registro: fields.registro.value,
        empresa: empresaSelecionada,
        cargo: fields.cargo.value,
        cidade: fields.cidade.value,
        estado: fields.estado.value,
        pre_requisitos: fields.requisitos.value,
        formacao: fields.formacao.value,
        conhecimentos_requeridos: fields.conhecimentos.value,
        regime: fields.regime.value,
        jornada_trabalho: fields.jornada.value,
        remuneracao: Number(fields.remuneracao.value)
            .toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'})
    };
    const editando = Boolean(fields.id.value);
    try {
        await request(editando ? `${api}/vagas` : `${api}/vagas/vaga`, {
            method: editando ? 'PUT' : 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        limparFormulario();
        await carregarVagas();
        setFeedback(editando ? 'Vaga atualizada.' : 'Vaga cadastrada.', 'ok');
    } catch (error) {
        setFeedback(error.message, 'error');
    }
});

tbody.addEventListener('click', async event => {
    const editId = event.target.dataset.edit;
    const deleteId = event.target.dataset.delete;
    if (editId) preencherEdicao(vagas.find(vaga => vaga.registro === editId));
    if (deleteId && confirm('Excluir esta vaga e seus registros de interesse?')) {
        try {
            await request(`${api}/vagas/${encodeURIComponent(deleteId)}`, {method: 'DELETE'});
            await carregarVagas();
            setFeedback('Vaga excluída.', 'ok');
        } catch (error) {
            setFeedback(error.message, 'error');
        }
    }
});

tbody.addEventListener('mouseover', event => {
    const thumbnail = event.target.closest('.info-chip');
    if (thumbnail) mostrarTooltip(thumbnail);
});
tbody.addEventListener('mouseout', event => {
    if (event.target.closest('.info-chip')) esconderTooltip();
});
tbody.addEventListener('focusin', event => {
    const thumbnail = event.target.closest('.info-chip');
    if (thumbnail) mostrarTooltip(thumbnail);
});
tbody.addEventListener('focusout', event => {
    if (event.target.closest('.info-chip')) esconderTooltip();
});

function preencherEdicao(vaga) {
    fields.id.value = vaga.registro;
    fields.registro.value = vaga.registro;
    fields.registro.disabled = true;
    fields.empresa.value = vaga.empresa.nome_fantasia;
    fields.cargo.value = vaga.cargo;
    fields.cidade.value = vaga.cidade;
    fields.estado.value = vaga.estado;
    fields.regime.value = vaga.regime;
    fields.jornada.value = vaga.jornada_trabalho;
    fields.remuneracao.value = moedaParaNumero(vaga.remuneracao);
    fields.formacao.value = vaga.formacao;
    fields.requisitos.value = vaga.pre_requisitos;
    fields.conhecimentos.value = vaga.conhecimentos_requeridos;
    document.querySelector('#form-title').textContent = 'Editar vaga';
    document.querySelector('#cancel-edit').classList.remove('hidden');
    document.querySelector('.form-panel').scrollIntoView({behavior: 'smooth'});
}

function moedaParaNumero(value) {
    return String(value).replace(/[^0-9,]/g, '').replace(',', '.');
}

function limparFormulario() {
    form.reset();
    fields.id.value = '';
    fields.registro.disabled = false;
    document.querySelector('#form-title').textContent = 'Nova vaga';
    document.querySelector('#cancel-edit').classList.add('hidden');
}

function setFeedback(message, type) {
    feedback.textContent = message;
    feedback.className = type;
}

function escapeHtml(value = '') {
    return String(value).replace(/[&<>'"]/g, char => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;'
    })[char]);
}

document.querySelector('#cancel-edit').addEventListener('click', limparFormulario);
document.querySelector('#refresh').addEventListener('click', () => carregarVagas().catch(showStartupError));
buscaVagas.addEventListener('input', renderizarVagas);
window.addEventListener('scroll', esconderTooltip, true);
window.addEventListener('resize', esconderTooltip);

function showStartupError(error) {
    setFeedback(error.message, 'error');
    document.querySelector('#api-status').textContent = 'API indisponível';
}

Promise.all([carregarCatalogos(), carregarVagas()])
    .then(() => document.querySelector('#api-status').textContent = 'API conectada')
    .catch(showStartupError);
