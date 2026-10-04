(function () {
  const keywordInput = document.getElementById('keyword');
  const locationInput = document.getElementById('location');
  const message = document.querySelector('[data-testid="message"]');
  const results = document.querySelector('[data-testid="results"]');

  function reset() {
    results.hidden = true;
    results.replaceChildren();
    message.textContent = '';
  }

  function render(jobs) {
    results.replaceChildren();
    for (const job of jobs) {
      const card = document.createElement('div');
      card.className = 'job-card';
      card.setAttribute('data-testid', 'job-card');
      card.tabIndex = 0;
      card.setAttribute('role', 'link');

      const title = document.createElement('div');
      title.className = 'job-title';
      title.setAttribute('data-testid', 'job-title');
      title.textContent = job.title;

      const loc = document.createElement('div');
      loc.className = 'job-location';
      loc.setAttribute('data-testid', 'job-location');
      loc.textContent = job.location;

      card.append(title, loc);
      const open = () => { window.location.href = '/jobs/' + job.id; };
      card.addEventListener('click', open);
      card.addEventListener('keydown', (e) => { if (e.key === 'Enter') open(); });
      results.appendChild(card);
    }
  }

  async function search() {
    const keyword = keywordInput.value.trim();
    const location = locationInput.value.trim();
    reset();
    if (!keyword && !location) {
      message.textContent = 'Enter a keyword or location';
      return;
    }
    const params = new URLSearchParams({ q: keyword, location: location });
    try {
      const response = await fetch('/api/search?' + params.toString());
      if (!response.ok) {
        message.textContent = 'Search is unavailable, please try again';
        return;
      }
      const jobs = await response.json();
      if (jobs.length === 0) {
        message.textContent = 'No jobs found';
        return;
      }
      render(jobs);
      results.hidden = false;
    } catch (err) {
      message.textContent = 'Search is unavailable, please try again';
    }
  }

  document.querySelector('[data-testid="search-button"]').addEventListener('click', search);
})();
