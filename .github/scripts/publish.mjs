import fs from 'node:fs';
import path from 'node:path';

const PLATFORM_ARG = process.argv.find(arg => arg.startsWith('--platform='))?.split('=')[1]
  || (process.argv.includes('--platform') ? process.argv[process.argv.indexOf('--platform') + 1] : 'all');

const MODRINTH_TOKEN = process.env.MODRINTH_TOKEN || '';
const CURSEFORGE_TOKEN = process.env.CURSEFORGE_TOKEN || '';
const CURSEFORGE_PROJECT_ID = process.env.CURSEFORGE_PROJECT_ID || '1658041';

function getChangelog() {
  try {
    const full = fs.readFileSync('CHANGELOG.md', 'utf-8');
    const start = full.indexOf('## [1.1.0]');
    if (start !== -1) {
      const end = full.indexOf('---', start);
      return (end !== -1 ? full.slice(start, end) : full.slice(start)).trim();
    }
    return full.trim();
  } catch (e) {
    console.warn('⚠️ Could not read CHANGELOG.md:', e.message);
    return 'QuickGlance release update';
  }
}

const changelog = getChangelog();

const targets = [
  {
    file: 'build/libs/quickglance-fabric-1.1.0+26.3.jar',
    name: 'QuickGlance 1.1.0 (Fabric 26.3)',
    versionNumber: '1.1.0+fabric-26.3',
    modrinth: {
      loaders: ['fabric', 'quilt'],
      gameVersions: ['26.3'],
    },
    curseforge: {
      gameVersionNames: ['Client', 'Server', 'Fabric', 'Quilt', '26.3'],
    },
  },
  {
    file: 'build/libs/quickglance-neoforge-1.1.0+26.3.jar',
    name: 'QuickGlance 1.1.0 (NeoForge 26.3)',
    versionNumber: '1.1.0+neoforge-26.3',
    modrinth: {
      loaders: ['neoforge'],
      gameVersions: ['26.3'],
    },
    curseforge: {
      gameVersionNames: ['Client', 'Server', 'NeoForge', '26.3'],
    },
  },
  {
    file: 'build/libs/quickglance-fabric-1.1.0+1.21.1.jar',
    name: 'QuickGlance 1.1.0 (Fabric 1.21.1)',
    versionNumber: '1.1.0+fabric-1.21.1',
    modrinth: {
      loaders: ['fabric', 'quilt'],
      gameVersions: ['1.21', '1.21.1'],
    },
    curseforge: {
      gameVersionNames: ['Client', 'Server', 'Fabric', 'Quilt', '1.21', '1.21.1'],
    },
  },
  {
    file: 'build/libs/quickglance-neoforge-1.1.0+1.21.1.jar',
    name: 'QuickGlance 1.1.0 (NeoForge 1.21.1)',
    versionNumber: '1.1.0+neoforge-1.21.1',
    modrinth: {
      loaders: ['neoforge'],
      gameVersions: ['1.21', '1.21.1'],
    },
    curseforge: {
      gameVersionNames: ['Client', 'Server', 'NeoForge', '1.21', '1.21.1'],
    },
  },
  {
    file: 'build/libs/quickglance-fabric-1.1.0+1.20.1.jar',
    name: 'QuickGlance 1.1.0 (Fabric 1.20.1)',
    versionNumber: '1.1.0+fabric-1.20.1',
    modrinth: {
      loaders: ['fabric', 'quilt'],
      gameVersions: ['1.20.1'],
    },
    curseforge: {
      gameVersionNames: ['Client', 'Server', 'Fabric', 'Quilt', '1.20.1'],
    },
  },
];

async function publishModrinth(target) {
  if (!MODRINTH_TOKEN) {
    console.log(`[Modrinth] Skipping ${target.name} (MODRINTH_TOKEN not set)`);
    return;
  }
  if (!fs.existsSync(target.file)) {
    throw new Error(`File not found: ${target.file}`);
  }

  console.log(`[Modrinth] Uploading ${target.name} (${target.versionNumber})...`);
  const form = new FormData();
  const metadata = {
    name: target.name,
    version_number: target.versionNumber,
    changelog: changelog,
    dependencies: [],
    game_versions: target.modrinth.gameVersions,
    version_type: 'release',
    loaders: target.modrinth.loaders,
    featured: false,
    status: 'listed',
    project_id: 'quickglance',
    file_parts: ['file_0'],
    primary_file: 'file_0',
  };
  form.append('data', JSON.stringify(metadata));
  const buf = fs.readFileSync(target.file);
  form.append('file_0', new Blob([buf]), path.basename(target.file));

  const res = await fetch('https://api.modrinth.com/v2/version', {
    method: 'POST',
    headers: {
      'Authorization': MODRINTH_TOKEN,
      'User-Agent': 'pixiemays/QuickGlance/1.1.0 (github.com/pixiemays/QuickGlance)',
    },
    body: form,
  });

  const text = await res.text();
  if (res.ok) {
    const data = JSON.parse(text);
    console.log(`[Modrinth] ✅ Successfully published ${target.name}! (ID: ${data.id})`);
  } else if (res.status === 400 && text.includes('already exists')) {
    console.log(`[Modrinth] ⚠️ Version ${target.versionNumber} already exists on Modrinth. Skipping.`);
  } else {
    throw new Error(`[Modrinth] ❌ HTTP ${res.status}: ${text}`);
  }
}

async function publishCurseForge(target) {
  if (!CURSEFORGE_TOKEN) {
    console.log(`[CurseForge] Skipping ${target.name} (CURSEFORGE_TOKEN not set)`);
    return;
  }
  if (!fs.existsSync(target.file)) {
    throw new Error(`File not found: ${target.file}`);
  }

  console.log(`[CurseForge] Uploading ${target.name}...`);
  const form = new FormData();
  const metadata = {
    displayName: target.name,
    changelog: changelog,
    changelogType: 'markdown',
    gameVersionNames: target.curseforge.gameVersionNames,
    releaseType: 'release',
  };
  form.append('metadata', JSON.stringify(metadata));
  const buf = fs.readFileSync(target.file);
  form.append('file', new Blob([buf]), path.basename(target.file));

  const res = await fetch(`https://minecraft.curseforge.com/api/projects/${CURSEFORGE_PROJECT_ID}/upload-file`, {
    method: 'POST',
    headers: {
      'X-Api-Token': CURSEFORGE_TOKEN,
    },
    body: form,
  });

  const text = await res.text();
  if (res.ok) {
    const data = JSON.parse(text);
    console.log(`[CurseForge] ✅ Successfully published ${target.name}! (File ID: ${data.id})`);
  } else {
    throw new Error(`[CurseForge] ❌ HTTP ${res.status}: ${text}`);
  }
}

async function main() {
  console.log(`Publishing QuickGlance 1.1.0... (Platform filter: ${PLATFORM_ARG})`);
  console.log(`Changelog preview: ${changelog.substring(0, 100)}...`);

  const runModrinth = PLATFORM_ARG === 'all' || PLATFORM_ARG === 'modrinth';
  const runCurseForge = PLATFORM_ARG === 'all' || PLATFORM_ARG === 'curseforge';

  const errors = [];

  if (runModrinth) {
    console.log('\n--- Publishing to Modrinth ---');
    for (const target of targets) {
      try {
        await publishModrinth(target);
      } catch (err) {
        console.error(err.message);
        errors.push(err);
      }
    }
  }

  if (runCurseForge) {
    console.log('\n--- Publishing to CurseForge ---');
    for (const target of targets) {
      try {
        await publishCurseForge(target);
      } catch (err) {
        console.error(err.message);
        errors.push(err);
      }
    }
  }

  if (errors.length > 0) {
    console.error(`\n❌ Completed with ${errors.length} error(s).`);
    process.exit(1);
  } else {
    console.log('\n🎉 All publishing operations finished successfully!');
  }
}

main().catch(err => {
  console.error('Fatal error:', err);
  process.exit(1);
});
