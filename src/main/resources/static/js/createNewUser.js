$(document).ready(function () {
    // Объект для работы с API
    const userApi = {
        // Получение всех пользователей
        getAllUsers: async () => {
            try {
                const response = await fetch('/api/users', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        // Добавьте CSRF-токен, если требуется
                        // 'X-CSRF-TOKEN': getCsrfToken()
                    }
                });
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                return await response.json();
            } catch (error) {
                console.error('Error fetching users:', error);
                throw error;
            }
        },

        // Получение пользователя по ID
        getUserById: async (id) => {
            try {
                const response = await fetch(`/api/users/${id}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                return await response.json();
            } catch (error) {
                console.error('Error fetching user:', error);
                throw error;
            }
        },

        // Получение текущего пользователя
        getCurrentUser: async () => {
            try {
                const response = await fetch('/api/current-user', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                return await response.json();
            } catch (error) {
                console.error('Error fetching current user:', error);
                throw error;
            }
        },

        // Создание нового пользователя
        createUser: async (userData) => {
            try {
                const response = await fetch('/api/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.info || `HTTP error! Status: ${response.status}`);
                }
                return await response.json();
            } catch (error) {
                console.error('Error creating user:', error);
                throw error;
            }
        },

        // Обновление пользователя
        updateUser: async (userData) => {
            try {
                const response = await fetch('/api/users', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.info || `HTTP error! Status: ${response.status}`);
                }
                return await response.json();
            } catch (error) {
                console.error('Error updating user:', error);
                throw error;
            }
        },

        // Удаление пользователя
        deleteUser: async (id) => {
            try {
                const response = await fetch(`/api/users/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                return true;
            } catch (error) {
                console.error('Error deleting user:', error);
                throw error;
            }
        }
    };

    // Функция для отображения уведомлений
    function showAlert(container, message, type = 'success') {
        const alert = `
            <div class="alert alert-${type} alert-dismissible fade show col-12" role="alert">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
        container.prepend(alert);
        setTimeout(() => $('.alert').alert('close'), 3000);
    }

    // Заполнение таблицы всех пользователей (Admin)
    async function fillAdminTable() {
        try {
            const users = await userApi.getAllUsers();
            const tbody = $('#users_home_table');
            tbody.empty();
            users.forEach(user => {
                // Проверяем, что роли существуют и обрабатываем их
                const roles = Array.isArray(user.roles) ? user.roles.map(role => role.name ? role.name.replace('ROLE_', '') : 'N/A').join(', ') : 'N/A';
                const row = `
                    <tr>
                        <td>${user.id || 'N/A'}</td>
                        <td>${user.username || 'N/A'}</td>
                        <td>${user.lastName || 'N/A'}</td>
                        <td>${user.age || 'N/A'}</td>
                        <td>${user.email || 'N/A'}</td>
                        <td>${roles}</td>
                        <td><button class="btn btn-primary btn-sm edit-user" data-id="${user.id}">Edit</button></td>
                        <td><button class="btn btn-danger btn-sm delete-user" data-id="${user.id}">Delete</button></td>
                    </tr>`;
                tbody.append(row);
            });
        } catch (error) {
            showAlert($('#adminTable'), 'Failed to load users: ' + error.message, 'danger');
        }
    }

    // Заполнение таблицы текущего пользователя (User)
    async function fillUserTable() {
        try {
            const user = await userApi.getCurrentUser();
            const tbody = $('#tableUser tbody');
            tbody.empty();
            const roles = Array.isArray(user.roles) ? user.roles.map(role => role.name ? role.name.replace('ROLE_', '') : 'N/A').join(', ') : 'N/A';
            const row = `
                <tr>
                    <td>${user.id || 'N/A'}</td>
                    <td>${user.username || 'N/A'}</td>
                    <td>${user.lastName || 'N/A'}</td>
                    <td>${user.age || 'N/A'}</td>
                    <td>${user.email || 'N/A'}</td>
                    <td>${roles}</td>
                </tr>`;
            tbody.append(row);
        } catch (error) {
            showAlert($('#userTable'), 'Failed to load user data: ' + error.message, 'danger');
        }
    }

    // Заполнение модального окна редактирования
    async function fillEditModal(userId) {
        try {
            const user = await userApi.getUserById(userId);
            const form = $('#editUserForm');
            form.find('#id1').val(user.id || '');
            form.find('#username1').val(user.username || '');
            form.find('#lastName1').val(user.lastName || '');
            form.find('#age1').val(user.age || '');
            form.find('#email1').val(user.email || '');
            form.find('#password1').val(''); // Пароль не заполняем
            // Устанавливаем выбранные роли
            const roleValues = Array.isArray(user.roles) ? user.roles.map(role => role.name) : [];
            form.find('#roles1').val(roleValues);
            $('#editUserModal').modal('show');
        } catch (error) {
            showAlert($('#adminTable'), 'Failed to load user data: ' + error.message, 'danger');
        }
    }

    // Создание нового пользователя
    function handleCreateUser() {
        $('#addForm').on('submit', async (event) => {
            event.preventDefault();
            const form = $('#addForm');
            const userData = {
                username: form.find('#usernameCreate').val().trim(),
                password: form.find('#passwordCreate').val().trim(),
                lastName: form.find('#surnameCreate').val().trim(),
                age: parseInt(form.find('#ageCreate').val().trim()),
                email: form.find('#emailCreate').val().trim(),
                roles: Array.from(form.find('#rolesCreate option:selected')).map(option => option.value)
            };

            if (!userData.username || !userData.password || !userData.lastName || !userData.age || !userData.email || !userData.roles.length) {
                showAlert(form, 'Please fill in all required fields', 'danger');
                return;
            }

            try {
                await userApi.createUser(userData);
                form.find('input').val('');
                form.find('#rolesCreate').val([]);
                showAlert(form, 'User created successfully!', 'success');
                await fillAdminTable();
                $('.nav-tabs a[href="#adminTable"]').tab('show');
            } catch (error) {
                showAlert(form, error.message, 'danger');
            }
        });
    }

    // Обновление пользователя
    function handleUpdateUser() {
        $('#editUserSubmit').on('click', async () => {
            const form = $('#editUserForm');
            const userData = {
                id: parseInt(form.find('#id1').val()),
                username: form.find('#username1').val().trim(),
                lastName: form.find('#lastName1').val().trim(),
                age: parseInt(form.find('#age1').val()),
                email: form.find('#email1').val().trim(),
                password: form.find('#password1').val().trim() || null,
                roles: Array.from(form.find('#roles1 option:selected')).map(option => option.value)
            };

            if (!userData.username || !userData.lastName || !userData.age || !userData.email || !userData.roles.length) {
                showAlert($('#editUserModal .modal-body'), 'Please fill in all required fields', 'danger');
                return;
            }

            try {
                await userApi.updateUser(userData);
                $('#editUserModal').modal('hide');
                showAlert($('#adminTable'), 'User updated successfully!', 'success');
                await fillAdminTable();
            } catch (error) {
                showAlert($('#editUserModal .modal-body'), error.message, 'danger');
            }
        });
    }

    // Удаление пользователя
    function handleDeleteUser() {
        $('#users_home_table').on('click', '.delete-user', async function () {
            const userId = $(this).data('id');
            if (confirm('Are you sure you want to delete this user?')) {
                try {
                    await userApi.deleteUser(userId);
                    showAlert($('#adminTable'), 'User deleted successfully!', 'success');
                    await fillAdminTable();
                } catch (error) {
                    showAlert($('#adminTable'), error.message, 'danger');
                }
            }
        });
    }

    // Обработчик редактирования пользователя
    function handleEditUser() {
        $('#users_home_table').on('click', '.edit-user', async function () {
            const userId = $(this).data('id');
            await fillEditModal(userId);
        });
    }

    // Инициализация
    async function init() {
        await fillAdminTable();
        await fillUserTable();
        handleCreateUser();
        handleUpdateUser();
        handleDeleteUser();
        handleEditUser();

        // Обновление таблиц при переключении вкладок
        $('a[href="#adminTable"]').on('shown.bs.tab', fillAdminTable);
        $('a[href="#userTable"]').on('shown.bs.tab', fillUserTable);
    }

    init();
});