// ─── Config ───────────────────────────────────────────────────────────────────

const API = 'http://localhost:7008/api';

// ─── State ────────────────────────────────────────────────────────────────────

const state = {
  token: localStorage.getItem('astralis_token'),
  user: (() => { try { return JSON.parse(localStorage.getItem('astralis_user')); } catch { return null; } })(),
  page: 'games',
  authTab: 'login',
  games: [],
  todos: [],
  qas: [],
  todoFilter: 'ALL',
};

// ─── API helper ───────────────────────────────────────────────────────────────

async function api(method, path, body = null) {
  const headers = { 'Content-Type': 'application/json' };
  if (state.token) headers['Authorization'] = `Bearer ${state.token}`;
  const opts = { method, headers };
  if (body != null) opts.body = JSON.stringify(body);
  const res = await fetch(API + path, opts);
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) throw new Error(data?.message || `Error ${res.status}`);
  return data;
}

// ─── Auth ─────────────────────────────────────────────────────────────────────

async function handleLogin(e) {
  e.preventDefault();
  const form = e.target;
  const btn = form.querySelector('button[type=submit]');
  const err = document.getElementById('auth-error');
  btn.disabled = true; btn.textContent = 'Signing in…'; err.textContent = '';
  try {
    const data = await api('POST', '/security/login', {
      username: form.username.value.trim(),
      password: form.password.value,
    });
    setSession(data);
    render();
  } catch (ex) {
    err.textContent = ex.message;
    btn.disabled = false; btn.textContent = 'Sign in';
  }
}

async function handleRegister(e) {
  e.preventDefault();
  const form = e.target;
  const btn = form.querySelector('button[type=submit]');
  const err = document.getElementById('auth-error');
  const username = form.username.value.trim();
  const password = form.password.value;
  btn.disabled = true; btn.textContent = 'Creating account…'; err.textContent = '';
  try {
    await api('POST', '/accounts/', { username, password });
    const data = await api('POST', '/security/login', { username, password });
    setSession(data);
    render();
  } catch (ex) {
    err.textContent = ex.message;
    btn.disabled = false; btn.textContent = 'Create account';
  }
}

function setSession(data) {
  state.token = data.token;
  state.user = data;
  localStorage.setItem('astralis_token', data.token);
  localStorage.setItem('astralis_user', JSON.stringify(data));
}

function logout() {
  state.token = null;
  state.user = null;
  localStorage.removeItem('astralis_token');
  localStorage.removeItem('astralis_user');
  render();
}

// ─── Navigation ───────────────────────────────────────────────────────────────

function navigate(page) {
  if (state.page === page) return;
  state.page = page;
  document.querySelectorAll('.nav-link').forEach(el =>
    el.classList.toggle('active', el.dataset.page === page)
  );
  loadPage(page);
}

function switchAuthTab(tab) {
  state.authTab = tab;
  document.getElementById('root').innerHTML = renderAuth();
  attachAuthListeners();
}

// ─── Data loading ─────────────────────────────────────────────────────────────

async function loadPage(page) {
  const content = document.getElementById('content');
  if (!content) return;
  content.innerHTML = '<div class="loading">Loading…</div>';
  try {
    switch (page) {
      case 'games':
        state.games = await api('GET', '/games/');
        content.innerHTML = renderGames();
        break;
      case 'todos':
        state.todos = await api('GET', '/todos/');
        content.innerHTML = renderTodos();
        break;
      case 'qa':
        state.qas = await api('GET', '/qas/');
        content.innerHTML = renderQA();
        break;
    }
  } catch (ex) {
    content.innerHTML = `<div class="error-msg" style="padding:40px">Failed to load: ${esc(ex.message)}</div>`;
  }
}

// ─── Games ────────────────────────────────────────────────────────────────────

async function submitGame(e) {
  e.preventDefault();
  const err = document.getElementById('modal-error');
  err.textContent = '';
  try {
    await api('POST', '/games/', { name: document.getElementById('game-name').value.trim(), accountId: state.user.id });
    closeModal();
    loadPage('games');
  } catch (ex) { err.textContent = ex.message; }
}

async function deleteGame(id) {
  if (!confirm('Delete this game?')) return;
  try { await api('DELETE', `/games/${id}`); loadPage('games'); }
  catch (ex) { alert(ex.message); }
}

// ─── Todos ────────────────────────────────────────────────────────────────────

async function submitTodo(e) {
  e.preventDefault();
  const err = document.getElementById('modal-error');
  err.textContent = '';
  try {
    await api('POST', '/todos/', {
      description: document.getElementById('todo-desc').value.trim(),
      status:      document.getElementById('todo-status').value,
      source:      document.getElementById('todo-source').value,
      account: { id: state.user.id },
    });
    closeModal();
    loadPage('todos');
  } catch (ex) { err.textContent = ex.message; }
}

async function setTodoStatus(id, status) {
  const todo = state.todos.find(t => t.id === id);
  if (!todo) return;
  try {
    await api('PUT', `/todos/${id}`, { ...todo, status, account: { id: todo.account?.id ?? state.user.id } });
    state.todos = await api('GET', '/todos/');
    document.getElementById('content').innerHTML = renderTodos();
  } catch (ex) { alert(ex.message); }
}

async function deleteTodo(id) {
  if (!confirm('Delete this todo?')) return;
  try { await api('DELETE', `/todos/${id}`); loadPage('todos'); }
  catch (ex) { alert(ex.message); }
}

function filterTodos(filter) {
  state.todoFilter = filter;
  document.querySelectorAll('.filter-btn').forEach(btn =>
    btn.classList.toggle('active', btn.dataset.filter === filter)
  );
  const filtered = filter === 'ALL' ? state.todos : state.todos.filter(t => t.status === filter);
  document.getElementById('todos-body').innerHTML = filtered.map(todoRow).join('');
}

// ─── Q&A ──────────────────────────────────────────────────────────────────────

async function submitQA(e) {
  e.preventDefault();
  const err = document.getElementById('modal-error');
  err.textContent = '';
  try {
    await api('POST', '/qas/', {
      question:  document.getElementById('qa-question').value.trim(),
      answer:    document.getElementById('qa-answer').value.trim(),
      accountId: state.user.id,
    });
    closeModal();
    loadPage('qa');
  } catch (ex) { err.textContent = ex.message; }
}

async function deleteQA(id) {
  if (!confirm('Delete this Q&A?')) return;
  try { await api('DELETE', `/qas/${id}`); loadPage('qa'); }
  catch (ex) { alert(ex.message); }
}

function toggleQA(id) {
  const body = document.getElementById(`qa-body-${id}`);
  const icon = document.getElementById(`qa-icon-${id}`);
  const open = body.classList.toggle('open');
  if (icon) icon.style.transform = open ? 'rotate(180deg)' : '';
}

// ─── Modal ────────────────────────────────────────────────────────────────────

function openModal(type) {
  const el = document.createElement('div');
  el.className = 'modal-backdrop';
  el.id = 'modal-backdrop';
  el.innerHTML = `
    <div class="modal" role="dialog" aria-modal="true">
      <div class="modal-header">
        <h3>${{ game: 'Add Game', todo: 'Add Todo', qa: 'Add Q&A' }[type]}</h3>
        <button class="icon-btn" onclick="closeModal()">✕</button>
      </div>
      <div class="modal-body">
        ${modalForm(type)}
        <p class="error-msg" id="modal-error"></p>
      </div>
    </div>`;
  el.addEventListener('click', e => { if (e.target === el) closeModal(); });
  document.body.appendChild(el);

  const form = el.querySelector('form');
  if (type === 'game') form.addEventListener('submit', submitGame);
  if (type === 'todo') form.addEventListener('submit', submitTodo);
  if (type === 'qa')   form.addEventListener('submit', submitQA);

  el.querySelector('input, textarea')?.focus();
}

function closeModal() {
  document.getElementById('modal-backdrop')?.remove();
}

function modalForm(type) {
  if (type === 'game') return `
    <form>
      <div class="form-group">
        <label>Game name</label>
        <input id="game-name" type="text" placeholder="e.g. Cyberpunk 2077" required>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" onclick="closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create</button>
      </div>
    </form>`;

  if (type === 'todo') return `
    <form>
      <div class="form-group">
        <label>Description</label>
        <textarea id="todo-desc" placeholder="Describe the task…" required rows="3"></textarea>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Status</label>
          <select id="todo-status">
            <option value="PENDING">Pending</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </div>
        <div class="form-group">
          <label>Source</label>
          <select id="todo-source">
            <option value="GAMEHUB">GameHub</option>
            <option value="STORE">Store</option>
          </select>
        </div>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" onclick="closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create</button>
      </div>
    </form>`;

  if (type === 'qa') return `
    <form>
      <div class="form-group">
        <label>Question</label>
        <input id="qa-question" type="text" placeholder="Enter your question…" required>
      </div>
      <div class="form-group">
        <label>Answer</label>
        <textarea id="qa-answer" placeholder="Enter the answer…" required rows="3"></textarea>
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" onclick="closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Add</button>
      </div>
    </form>`;

  return '';
}

// ─── Render ───────────────────────────────────────────────────────────────────

function render() {
  const root = document.getElementById('root');
  if (!state.token) {
    root.innerHTML = renderAuth();
    attachAuthListeners();
  } else {
    root.innerHTML = renderShell();
    loadPage(state.page);
  }
}

function renderAuth() {
  const isLogin = state.authTab === 'login';
  return `
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-brand">
        <span class="brand-icon">✦</span>
        <h1>Astralis</h1>
        <p>Quality Assurance Platform</p>
      </div>
      <div class="auth-tabs">
        <button class="auth-tab ${isLogin ? 'active' : ''}" onclick="switchAuthTab('login')">Sign in</button>
        <button class="auth-tab ${!isLogin ? 'active' : ''}" onclick="switchAuthTab('register')">Register</button>
      </div>
      ${isLogin ? `
      <form class="auth-form" id="login-form">
        <div class="form-group">
          <label>Username</label>
          <input name="username" type="text" placeholder="Enter username" autocomplete="username" required>
        </div>
        <div class="form-group">
          <label>Password</label>
          <input name="password" type="password" placeholder="Enter password" autocomplete="current-password" required>
        </div>
        <button type="submit" class="btn btn-primary btn-full">Sign in</button>
      </form>` : `
      <form class="auth-form" id="register-form">
        <div class="form-group">
          <label>Username</label>
          <input name="username" type="text" placeholder="Choose a username" autocomplete="username" required>
        </div>
        <div class="form-group">
          <label>Password</label>
          <input name="password" type="password" placeholder="Choose a password" autocomplete="new-password" required>
        </div>
        <button type="submit" class="btn btn-primary btn-full">Create account</button>
      </form>`}
      <p class="error-msg" id="auth-error"></p>
    </div>
  </div>`;
}

function attachAuthListeners() {
  document.getElementById('login-form')?.addEventListener('submit', handleLogin);
  document.getElementById('register-form')?.addEventListener('submit', handleRegister);
}

function renderShell() {
  const u = state.user;
  return `
  <div class="app">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-icon">✦</span>
        <span class="brand-name">Astralis</span>
      </div>
      <nav class="nav">
        ${navLink('games', '🎮', 'Games')}
        ${navLink('todos', '✓', 'Todos')}
        ${navLink('qa',    '?', 'Q&A')}
      </nav>
      <div class="sidebar-footer">
        <div class="user-chip">
          <div class="user-avatar">${esc((u?.username || 'U')[0].toUpperCase())}</div>
          <div class="user-info">
            <span class="user-name">${esc(u?.username || 'User')}</span>
            <span class="user-role">${esc(u?.role || '')}</span>
          </div>
        </div>
        <button class="btn btn-ghost btn-sm btn-full" onclick="logout()">Sign out</button>
      </div>
    </aside>
    <main class="main">
      <div id="content" class="content"></div>
    </main>
  </div>`;
}

function navLink(page, icon, label) {
  return `<button class="nav-link ${state.page === page ? 'active' : ''}" data-page="${page}" onclick="navigate('${page}')">
    <span class="nav-icon">${icon}</span>${label}
  </button>`;
}

// ─── Games page ───────────────────────────────────────────────────────────────

function renderGames() {
  const g = state.games;
  return `
  <div class="page-header">
    <div>
      <h2>Games</h2>
      <p class="text-muted">${g.length} game${g.length !== 1 ? 's' : ''}</p>
    </div>
    <button class="btn btn-primary" onclick="openModal('game')">+ Add Game</button>
  </div>
  ${g.length === 0
    ? `<div class="empty-state">
        <p>No games registered yet.</p>
        <button class="btn btn-primary" onclick="openModal('game')">Add your first game</button>
       </div>`
    : `<div class="game-grid">${g.map(gameCard).join('')}</div>`}`;
}

function gameCard(g) {
  return `
  <div class="game-card">
    <div class="game-card-header">
      <div class="game-icon">${esc(g.name[0].toUpperCase())}</div>
      <button class="icon-btn danger" onclick="deleteGame(${g.id})" title="Delete">✕</button>
    </div>
    <h3 class="game-name">${esc(g.name)}</h3>
    <p class="text-muted" style="font-size:12px">ID: ${g.id}</p>
  </div>`;
}

// ─── Todos page ───────────────────────────────────────────────────────────────

function renderTodos() {
  const all = state.todos;
  const counts = { ALL: all.length };
  ['PENDING', 'IN_PROGRESS', 'COMPLETED'].forEach(s => counts[s] = all.filter(t => t.status === s).length);
  const filtered = state.todoFilter === 'ALL' ? all : all.filter(t => t.status === state.todoFilter);

  return `
  <div class="page-header">
    <div>
      <h2>Todos</h2>
      <p class="text-muted">${all.length} task${all.length !== 1 ? 's' : ''}</p>
    </div>
    <button class="btn btn-primary" onclick="openModal('todo')">+ Add Todo</button>
  </div>
  <div class="filter-bar">
    ${[['ALL','All'],['PENDING','Pending'],['IN_PROGRESS','In Progress'],['COMPLETED','Completed']].map(([f, l]) => `
      <button class="filter-btn ${state.todoFilter === f ? 'active' : ''}" data-filter="${f}" onclick="filterTodos('${f}')">
        ${l} <span class="count-badge">${counts[f]}</span>
      </button>`).join('')}
  </div>
  ${filtered.length === 0
    ? '<div class="empty-state"><p>No tasks found.</p></div>'
    : `<div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Description</th>
              <th>Status</th>
              <th>Source</th>
              <th>Date</th>
              <th>Change status</th>
              <th></th>
            </tr>
          </thead>
          <tbody id="todos-body">${filtered.map(todoRow).join('')}</tbody>
        </table>
      </div>`}`;
}

function todoRow(t) {
  const statusCls = t.status.toLowerCase().replace('_', '-');
  return `
  <tr>
    <td>${esc(t.description)}</td>
    <td><span class="badge status-${statusCls}">${statusLabel(t.status)}</span></td>
    <td><span class="badge source-${(t.source || '').toLowerCase()}">${esc(t.source) || '—'}</span></td>
    <td class="text-muted">${t.date || '—'}</td>
    <td>
      <select class="status-select" onchange="setTodoStatus(${t.id}, this.value)">
        <option value="PENDING"     ${t.status === 'PENDING'     ? 'selected' : ''}>Pending</option>
        <option value="IN_PROGRESS" ${t.status === 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
        <option value="COMPLETED"   ${t.status === 'COMPLETED'   ? 'selected' : ''}>Completed</option>
      </select>
    </td>
    <td><button class="icon-btn danger" onclick="deleteTodo(${t.id})">✕</button></td>
  </tr>`;
}

// ─── Q&A page ─────────────────────────────────────────────────────────────────

function renderQA() {
  const qas = state.qas;
  return `
  <div class="page-header">
    <div>
      <h2>Q&amp;A</h2>
      <p class="text-muted">${qas.length} entr${qas.length !== 1 ? 'ies' : 'y'}</p>
    </div>
    <button class="btn btn-primary" onclick="openModal('qa')">+ Add Q&amp;A</button>
  </div>
  ${qas.length === 0
    ? `<div class="empty-state">
        <p>No Q&amp;A entries yet.</p>
        <button class="btn btn-primary" onclick="openModal('qa')">Add first entry</button>
       </div>`
    : `<div class="qa-list">${qas.map(qaItem).join('')}</div>`}`;
}

function qaItem(qa) {
  return `
  <div class="qa-item">
    <div class="qa-question" onclick="toggleQA(${qa.id})">
      <span class="qa-q-text">${esc(qa.question)}</span>
      <div class="qa-actions">
        <button class="icon-btn danger" onclick="event.stopPropagation(); deleteQA(${qa.id})">✕</button>
        <span class="qa-chevron" id="qa-icon-${qa.id}">▾</span>
      </div>
    </div>
    <div class="qa-body" id="qa-body-${qa.id}">
      <p class="qa-answer">${esc(qa.answer)}</p>
    </div>
  </div>`;
}

// ─── Utils ────────────────────────────────────────────────────────────────────

function esc(s) {
  return String(s ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function statusLabel(s) {
  return { PENDING: 'Pending', IN_PROGRESS: 'In Progress', COMPLETED: 'Completed' }[s] || s;
}

// ─── Init ─────────────────────────────────────────────────────────────────────

render();
