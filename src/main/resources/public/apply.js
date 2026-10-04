(function () {
  const button = document.querySelector('[data-testid="apply-button"]');
  const form = document.querySelector('[data-testid="application-form"]');
  button.addEventListener('click', () => { form.hidden = false; });
  form.addEventListener('submit', (e) => {
    e.preventDefault();
    form.hidden = true;
    document.querySelector('[data-testid="message"]').textContent = 'Application submitted';
  });
})();
