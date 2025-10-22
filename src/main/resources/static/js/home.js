fetch('/api/users').then(response => {
    return response.json();
}).then(data => {
    console.log('Данные пользователя: ' , data);
    
    document.getElementById('users_home_table').innerHTML = userCard;

}).catch(error =>{
    alert("Ошибка при загрузке данных: " + error)
});

let refreshInterval;

async function getAllUsers(showAlert = false){
    try {
        if (showAlert) {
            alert("Загрузка пользователей с базы данных");
        }
        
        // Показываем индикатор загрузки
        document.getElementById('users_home_table').innerHTML = `
            <div style="text-align: center; padding: 20px;">
                <div>Загрузка данных... ⏳</div>
            </div>
        `;
        
        const response = await fetch('/api/users');
        if (!response.ok) throw new Error('Ошибка при загрузке пользователей');
        
        const users = await response.json();
        
        const tableHTML = `
            <table class="home-users_table">
                <tbody>
                    ${users.map(user => {
                        let rolesDisplay = '';
                        if (Array.isArray(user.role)) {
                            rolesDisplay = user.role.join(', ');
                        } else if (typeof user.role === 'string' && user.role.includes(',')) {
                            rolesDisplay = user.role.split(',').map(r => r.trim()).join(', ');
                        } else {
                            rolesDisplay = user.role || '';
                        }
                        
                        return `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username || ''}</td>
                            <td>${user.lastName || ''}</td>
                            <td>${user.age || ''}</td>
                            <td>${user.email || ''}</td>
                            <td>${rolesDisplay}</td>
                            <td>
                                <button onclick="editUser(${user.id})" class="btn-edit">Edit</button>
                            </td>
                            <td>
                                <button onclick="deleteUser(${user.id})" class="btn-delete">Delete</button>
                            </td>
                        </tr>
                        `;
                    }).join('')}
                </tbody>
            </table>
            <div style="text-align: center; margin-top: 10px; color: #666; font-size: 12px;">
                ✅ Данные обновлены: ${new Date().toLocaleTimeString()} | 
                Следующее обновление через 5 секунд
            </div>
        `;
        
        document.getElementById('users_home_table').innerHTML = tableHTML;
        
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('users_home_table').innerHTML = `
            <div style="text-align: center; padding: 20px; color: red;">
                ❌ Ошибка загрузки данных: ${error.message}
                <br>
                <button onclick="getAllUsers(true)" style="margin-top: 10px;">Повторить попытку</button>
            </div>
        `;
        
        if (showAlert) {
            alert('Не удалось загрузить пользователей: ' + error.message);
        }
    }
}

// Запуск автоматического обновления
function startAutoRefresh() {
    // Первая загрузка с алертом
    getAllUsers(true);
    
    // Последующие без алерта
    refreshInterval = setInterval(() => getAllUsers(false), 5000);
}

// Остановка автоматического обновления
function stopAutoRefresh() {
    if (refreshInterval) {
        clearInterval(refreshInterval);
        refreshInterval = null;
    }
}

// Запускаем при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    startAutoRefresh();
});

// Вызываем функцию при загрузке страницы или по клику
getAllUsers();


