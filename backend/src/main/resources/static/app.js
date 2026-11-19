const api = '/api';
const form = document.querySelector('#vaga-form');
const feedback = document.querySelector('#feedback');
const tbody = document.querySelector('#vagas-body');
let vagas = [];

const fields = {
    id: document.querySelector('#vaga-id'),
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
    const [empresas, cargos] = await Promise.all([
        request(`${api}/empresas`),
        request(`${api}/cargos`)
    ]);
    fields.empresa.innerHTML = empresas.map(item =>
        `<option value="${item.id}">${escapeHtml(item.nomeFantasia)}</option>`).join('');
    fields.cargo.innerHTML = cargos.map(item =>
        `<option value="${item.id}">${escapeHtml(item.nome)}</option>`).join('');
}

async function carregarVagas() {
    vagas = await request(`${api}/vagas`);
    renderizarVagas();
}

function renderizarVagas() {
    document.querySelector('#empty').classList.toggle('hidden', vagas.length > 0);
    tbody.innerHTML = vagas.map(vaga => `
        <tr>
            <td><strong>${escapeHtml(vaga.cargo.nome)}</strong></td>
            <td>${escapeHtml(vaga.empresa.nomeFantasia)}</td>
            <td>${escapeHtml(vaga.cidade)}/${escapeHtml(vaga.estado)}</td>
            <td>${escapeHtml(vaga.regime)}</td>
            <td>${formatarMoeda(vaga.remuneracao)}</td>
            <td class="actions">
                <button class="button ghost small" data-edit="${vaga.id}">Editar</button>
                <button class="button danger small" data-delete="${vaga.id}">Excluir</button>
            </td>
        </tr>`).join('');
}

form.addEventListener('submit', async event => {
    event.preventDefault();
    setFeedback('Salvando...', '');
    const body = {
        empresaId: fields.empresa.value,
        cargoId: fields.cargo.value,
        cidade: fields.cidade.value,
        estado: fields.estado.value,
        requisitos: fields.requisitos.value,
        formacao: fields.formacao.value,
        conhecimentos: fields.conhecimentos.value,
        regime: fields.regime.value,
        jornada: fields.jornada.value,
        remuneracao: Number(fields.remuneracao.value)
    };
    const id = fields.id.value;
    try {
        await request(id ? `${api}/vagas/${id}` : `${api}/vagas`, {
            method: id ? 'PUT' : 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        limparFormulario();
        await carregarVagas();
        setFeedback(id ? 'Vaga atualizada.' : 'Vaga cadastrada.', 'ok');
    } catch (error) {
        setFeedback(error.message, 'error');
    }
});

tbody.addEventListener('click', async event => {
    const editId = event.target.dataset.edit;
    const deleteId = event.target.dataset.delete;
    if (editId) preencherEdicao(vagas.find(vaga => vaga.id === editId));
    if (deleteId && confirm('Excluir esta vaga e seus registros de interesse?')) {
        try {
            await request(`${api}/vagas/${deleteId}`, {method: 'DELETE'});
            await carregarVagas();
            setFeedback('Vaga excluída.', 'ok');
        } catch (error) {
            setFeedback(error.message, 'error');
        }
    }
});

function preencherEdicao(vaga) {
    fields.id.value = vaga.id;
    fields.empresa.value = vaga.empresa.id;
    fields.cargo.value = vaga.cargo.id;
    ['cidade', 'estado', 'regime', 'jornada', 'remuneracao', 'formacao', 'requisitos', 'conhecimentos']
        .forEach(name => fields[name].value = vaga[name]);
    document.querySelector('#form-title').textContent = 'Editar vaga';
    document.querySelector('#cancel-edit').classList.remove('hidden');
    document.querySelector('.form-panel').scrollIntoView({behavior: 'smooth'});
}

function limparFormulario() {
    form.reset();
    fields.id.value = '';
    document.querySelector('#form-title').textContent = 'Nova vaga';
    document.querySelector('#cancel-edit').classList.add('hidden');
}

function setFeedback(message, type) {
    feedback.textContent = message;
    feedback.className = type;
}

function formatarMoeda(value) {
    return Number(value).toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'});
}

function escapeHtml(value = '') {
    return String(value).replace(/[&<>'"]/g, char => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;'
    })[char]);
}

document.querySelector('#cancel-edit').addEventListener('click', limparFormulario);
document.querySelector('#refresh').addEventListener('click', () => carregarVagas().catch(showStartupError));

function showStartupError(error) {
    setFeedback(error.message, 'error');
    document.querySelector('#api-status').textContent = 'API indisponível';
}

Promise.all([carregarCatalogos(), carregarVagas()])
    .then(() => document.querySelector('#api-status').textContent = 'API conectada')
    .catch(showStartupError);
